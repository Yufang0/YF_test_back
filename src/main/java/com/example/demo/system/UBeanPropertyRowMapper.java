package com.example.demo.system;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EmbeddedId;

public class UBeanPropertyRowMapper<T> implements RowMapper<T> {

    private Field pkField;

    /** Logger available to subclasses. */
    protected final Log logger = LogFactory.getLog(getClass());

    /** The class we are mapping to. */
    @Nullable
    private Class<T> mappedClass;

    @Nullable
    private Class<T> mappedPKClass;

    /** Whether we're strictly validating. */
    private boolean checkFullyPopulated = false;

    /** Whether we're defaulting primitives when mapping a null value. */
    private boolean primitivesDefaultedForNullValue = false;

    /** ConversionService for binding JDBC values to bean properties. */
    @Nullable
    private ConversionService conversionService = DefaultConversionService.getSharedInstance();

    /** Map of the fields we provide mapping for. */
    @Nullable
    private Map<String, PropertyDescriptor> mappedFields;

    @Nullable
    private Map<String, PropertyDescriptor> mappedPKFields;

    /** Set of bean properties we provide mapping for. */
    @Nullable
    private Set<String> mappedProperties;

    @Nullable
    private Set<String> mappedPKProperties;

    /**
     * Create a new {@code BeanPropertyRowMapper} for bean-style configuration.
     * 
     * @see #setMappedClass
     * @see #setCheckFullyPopulated
     */
    public UBeanPropertyRowMapper() {
    }

    /**
     * Create a new {@code BeanPropertyRowMapper}, accepting unpopulated
     * properties in the target bean.
     * <p>
     * Consider using the {@link #newInstance} factory method instead,
     * which allows for specifying the mapped type once only.
     * 
     * @param mappedClass the class that each row should be mapped to
     */
    public UBeanPropertyRowMapper(Class<T> mappedClass) {
        initialize(mappedClass);
    }

    /**
     * Create a new {@code BeanPropertyRowMapper}.
     * 
     * @param mappedClass         the class that each row should be mapped to
     * @param checkFullyPopulated whether we're strictly validating that
     *                            all bean properties have been mapped from
     *                            corresponding database fields
     */
    public UBeanPropertyRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
        initialize(mappedClass);
        this.checkFullyPopulated = checkFullyPopulated;
    }

    /**
     * Set the class that each row should be mapped to.
     */
    public void setMappedClass(Class<T> mappedClass) {
        if (this.mappedClass == null) {
            initialize(mappedClass);
        } else {
            if (this.mappedClass != mappedClass) {
                throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " +
                        mappedClass + " since it is already providing mapping for " + this.mappedClass);
            }
        }
    }

    /**
     * Get the class that we are mapping to.
     */
    @Nullable
    public final Class<T> getMappedClass() {
        return this.mappedClass;
    }

    /**
     * Set whether we're strictly validating that all bean properties have been
     * mapped
     * from corresponding database fields.
     * <p>
     * Default is {@code false}, accepting unpopulated properties in the target
     * bean.
     */
    public void setCheckFullyPopulated(boolean checkFullyPopulated) {
        this.checkFullyPopulated = checkFullyPopulated;
    }

    /**
     * Return whether we're strictly validating that all bean properties have been
     * mapped from corresponding database fields.
     */
    public boolean isCheckFullyPopulated() {
        return this.checkFullyPopulated;
    }

    /**
     * Set whether we're defaulting Java primitives in the case of mapping a null
     * value
     * from corresponding database fields.
     * <p>
     * Default is {@code false}, throwing an exception when nulls are mapped to Java
     * primitives.
     */
    public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
        this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
    }

    /**
     * Return whether we're defaulting Java primitives in the case of mapping a null
     * value
     * from corresponding database fields.
     */
    public boolean isPrimitivesDefaultedForNullValue() {
        return this.primitivesDefaultedForNullValue;
    }

    /**
     * Set a {@link ConversionService} for binding JDBC values to bean properties,
     * or {@code null} for none.
     * <p>
     * Default is a {@link DefaultConversionService}, as of Spring 4.3. This
     * provides support for {@code java.time} conversion and other special types.
     * 
     * @since 4.3
     * @see #initBeanWrapper(BeanWrapper)
     */
    public void setConversionService(@Nullable ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * Return a {@link ConversionService} for binding JDBC values to bean
     * properties,
     * or {@code null} if none.
     * 
     * @since 4.3
     */
    @Nullable
    public ConversionService getConversionService() {
        return this.conversionService;
    }

    /**
     * Initialize the mapping meta-data for the given class.
     * 
     * @param mappedClass the mapped class
     */
    protected void initialize(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap<>();
        this.mappedProperties = new HashSet<>();

        this.mappedPKFields = new HashMap<>();
        this.mappedPKProperties = new HashSet<>();

        String pkFields = "";
        for (Field f : FieldUtils.getAllFieldsList(mappedClass)) {
            EmbeddedId emb = AnnotationUtils.findAnnotation(f, EmbeddedId.class);
            if (emb != null) {
                pkFields = f.getName();
                pkField = f;
            }
        }

        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null) {

                if (pd.getName().equals(pkFields)) {

                    Class<?> pkClass = pkField.getType();
                    this.mappedPKClass = (Class<T>) pkClass;
                    PropertyDescriptor[] pds2 = BeanUtils.getPropertyDescriptors(pkClass);
                    for (PropertyDescriptor pd2 : pds2) {
                        this.mappedPKFields.put(lowerFirst(pd2.getName()), pd2);
                        this.mappedPKProperties.add(lowerFirst(pd2.getName()));
                    }

                } else {
                    this.mappedFields.put(lowerFirst(pd.getName()), pd);
                    this.mappedProperties.add(lowerFirst(pd.getName()));
                }
            }
        }
    }

    private String lowerFirst(String name) {
        if (name == null || "".equals(name))
            return name;
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    /**
     * Extract the values for all columns in the current row.
     * <p>
     * Utilizes public setters and result set meta-data.
     * 
     * @see java.sql.ResultSetMetaData
     */
    @Override
    public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        T mappedObject = BeanUtils.instantiateClass(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        initBeanWrapper(bw);

        T mappedPKObject = null;
        BeanWrapper pkbw = null;
        if (this.mappedPKClass != null) {
            mappedPKObject = BeanUtils.instantiateClass(this.mappedPKClass);
            pkbw = PropertyAccessorFactory.forBeanPropertyAccess(mappedPKObject);
            initBeanWrapper(pkbw);
        }

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<>() : null);

        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            String field = underscore2Camel(column);
            PropertyDescriptor pd = (this.mappedFields != null ? this.mappedFields.get(field) : null);
            if (pd != null) {
                try {
                    Object value = getColumnValue(rs, index, pd);
                    // if (rowNumber == 0 && logger.isDebugEnabled()) {
                    // logger.debug("Mapping column '" + column + "' to property '" + pd.getName() +
                    // "' of type '" + ClassUtils.getQualifiedName(pd.getPropertyType()) + "'");
                    // }
                    try {
                        bw.setPropertyValue(pd.getName(), value);
                    } catch (TypeMismatchException ex) {
                        if (value == null && this.primitivesDefaultedForNullValue) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Intercepted TypeMismatchException for row " + rowNumber +
                                        " and column '" + column + "' with null value when setting property '" +
                                        pd.getName() + "' of type '" +
                                        ClassUtils.getQualifiedName(pd.getPropertyType()) +
                                        "' on object: " + mappedObject, ex);
                            }
                        } else {
                            throw ex;
                        }
                    }
                    if (populatedProperties != null) {
                        populatedProperties.add(pd.getName());
                    }
                } catch (NotWritablePropertyException ex) {
                    throw new DataRetrievalFailureException(
                            "Unable to map column '" + column + "' to property '" + pd.getName() + "'", ex);
                }
            } else {
                // No PropertyDescriptor found
                if (rowNumber == 0 && logger.isDebugEnabled()) {
                    logger.debug("No property found for column '" + column + "' mapped to field '" + field + "'");
                }
            }

            PropertyDescriptor pkpd = (this.mappedPKFields != null ? this.mappedPKFields.get(field) : null);
            if (pkpd != null) {
                try {
                    Object value = getColumnValue(rs, index, pkpd);
                    // if (rowNumber == 0 && logger.isDebugEnabled()) {
                    // logger.debug("Mapping column '" + column + "' to property '" + pkpd.getName()
                    // +
                    // "' of type '" + ClassUtils.getQualifiedName(pkpd.getPropertyType()) + "'");
                    // }
                    try {
                        pkbw.setPropertyValue(pkpd.getName(), value);
                    } catch (TypeMismatchException ex) {
                        if (value == null && this.primitivesDefaultedForNullValue) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Intercepted TypeMismatchException for row " + rowNumber +
                                        " and column '" + column + "' with null value when setting property '" +
                                        pkpd.getName() + "' of type '" +
                                        ClassUtils.getQualifiedName(pkpd.getPropertyType()) +
                                        "' on object: " + mappedObject, ex);
                            }
                        } else {
                            throw ex;
                        }
                    }
                    if (populatedProperties != null) {
                        populatedProperties.add(pkpd.getName());
                    }
                } catch (NotWritablePropertyException ex) {
                    throw new DataRetrievalFailureException(
                            "Unable to map column '" + column + "' to property '" + pd.getName() + "'", ex);
                }

            }
        }

        if (this.mappedPKClass != null) {
            try {
                pkField.setAccessible(true);
                pkField.set(mappedObject, mappedPKObject);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
            throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields " +
                    "necessary to populate object of class [" + this.mappedClass.getName() + "]: " +
                    this.mappedProperties);
        }

        return mappedObject;
    }

    /**
     * Initialize the given BeanWrapper to be used for row mapping.
     * To be called for each row.
     * <p>
     * The default implementation applies the configured {@link ConversionService},
     * if any. Can be overridden in subclasses.
     * 
     * @param bw the BeanWrapper to initialize
     * @see #getConversionService()
     * @see BeanWrapper#setConversionService
     */
    protected void initBeanWrapper(BeanWrapper bw) {
        ConversionService cs = getConversionService();
        if (cs != null) {
            bw.setConversionService(cs);
        }
    }

    /**
     * Retrieve a JDBC object value for the specified column.
     * <p>
     * The default implementation calls
     * {@link JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)}.
     * Subclasses may override this to check specific value types upfront,
     * or to post-process values return from {@code getResultSetValue}.
     * 
     * @param rs    is the ResultSet holding the data
     * @param index is the column index
     * @param pd    the bean property that each result object is expected to match
     * @return the Object value
     * @throws SQLException in case of extraction failure
     * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue(java.sql.ResultSet,
     *      int, Class)
     */
    @Nullable
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        // 以下為了JSON NODE加的
        Class<?> propertyType = pd.getPropertyType();

        if (JsonNode.class.isAssignableFrom(propertyType)) {
            String json = rs.getString(index);
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readTree(json);
            } catch (IOException e) {
                throw new SQLException("Failed to convert JSON string to JsonNode", e);
            }
        }
        // 以上為了JSON NODE加的
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

    /**
     * Static factory method to create a new {@code BeanPropertyRowMapper}
     * (with the mapped class specified only once).
     * 
     * @param mappedClass the class that each row should be mapped to
     */
    public static <T> UBeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
        return new UBeanPropertyRowMapper<>(mappedClass);
    }

    /**
     * convert underscore case to camel case <br>
     * for exmpale, MY_NAME => myName
     * 
     * @param columnName
     * @return
     */
    private String underscore2Camel(String columnName) {
        if (columnName == null || "".equals(columnName))
            return "";
        String[] strArr = columnName.toLowerCase().split("_");
        StringBuffer sb = new StringBuffer();
        for (String str : strArr) {
            if (str == null || str.trim().isEmpty())
                continue;
            if (sb.length() == 0) {
                sb.append(str);
            } else {
                sb.append(str.substring(0, 1).toUpperCase()).append(str.substring(1));
            }
        }
        return sb.toString();
    }
}
