/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012] - [2013] Codenvy, S.A. 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.api.project.shared;

import com.codenvy.api.resources.shared.AttributeProvider;
import com.codenvy.api.vfs.shared.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * Attribute of Project. Attribute may be hierarchical; one Attribute may contain other set of child attributes.
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @see AttributeProvider
 */
public class Attribute {
    /** Implementation of AttributeValueProvider which uses Property. */
    private static class DefaultValueProvider implements AttributeValueProvider {
        final Property property;

        DefaultValueProvider(Property property) {
            this.property = property;
        }

        public String getValue() {
            final List<String> values = property.getValue();
            if (!(values == null || values.isEmpty())) {
                return values.get(0);
            }
            return null;
        }

        public void setValue(String value) {
            if (value != null) {
                final List<String> list = new ArrayList<>(1);
                list.add(value);
                property.setValue(list);
            } else {
                property.setValue(null);
            }
        }
    }

    private final AttributeValueProvider valueProvider;
    private final String                 name;
    private       List<Attribute>        children;

    /**
     * Creates new Attribute with specified <code>name</code> and use specified <code>AttributeValueProvider</code> to reading and updating
     * value of this Attribute.
     */
    public Attribute(String name, AttributeValueProvider valueProvider) {
        this.name = name;
        this.valueProvider = valueProvider;
    }

    /**
     * Creates new Attribute and read name and value of Attribute from specified property. If Property has multiple value read first value
     * of Property.
     */
    public Attribute(Property property) {
        this.name = property.getName();
        this.valueProvider = new DefaultValueProvider(property);
    }

    /** Name of attribute. */
    public String getName() {
        return name;
    }

    /**
     * Get value of attribute.
     *
     * @return current value of attribute
     */
    public final String getValue() {
        return valueProvider.getValue();
    }

    /**
     * Set value of attribute.
     *
     * @param value
     *         new value of attribute
     */
    public final void setValue(String value) {
        valueProvider.setValue(value);
    }

    /**
     * Add child Attribute to this Attribute.
     *
     * @param child
     *         Attribute to add
     * @throws IllegalArgumentException
     *         if specified attribute is {code null}
     */
    public void addChild(Attribute child) {
        if (child == null) {
            throw new IllegalArgumentException("Null attribute is not allowed. ");
        }
        if (children == null) {
            children = new ArrayList<>(4);
        }
        children.add(child);
    }

    /** Get child Attribute by {@code name}. */
    public Attribute getChild(String name) {
        if (children == null) {
            return null;
        }
        for (Attribute child : children) {
            if (name.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

    /**
     * Get list of child Attribute. In case if this Attribute hasn't child this method returns empty {@code List} never {2code null}.
     * Modifications to the returned {@code List} will not affect the internal {@code List}.
     */
    public List<Attribute> getChildren() {
        return children == null ? new ArrayList<Attribute>(2) : new ArrayList<>(children);
    }

    /** Tests whether this Attribute has child Attributes. */
    public boolean hasChildren() {
        return !(children == null || children.isEmpty());
    }
}