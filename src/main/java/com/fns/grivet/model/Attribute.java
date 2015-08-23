/*
 * Copyright 2015 - Chris Phillipson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fns.grivet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * An {@code Attribute} participates in the definition of a property in a {@link ClassAttribute}.
 * One or more {@code ClassAttribute} may share an {@code Attribute}.
 * 
 * @author Chris Phillipson
 */
@Entity
public class Attribute extends Audited {

    /** 
     * Version number used during deserialization to verify that the sender and receiver 
     * of this serialized object have loaded classes for this object that 
     * are compatible with respect to serialization. 
     */
    private static final long serialVersionUID = 1L;

    /** The id. Auto-generated by the underyling data-store. */
    @Id
    @GeneratedValue
    private Integer id;

    /** The name of the attribute. Must be non-null, unique and less than 256 characters. */
    @Size(max=255)
    @Column(nullable=false, unique=true)
    private String name;
    
    /** The description of the attribute. Optional, but must be less than equal to 1000 characters. */
    @Column
    private String description;
    
    /**
     * For internal use only! Instantiates a new attribute.
     */
    protected Attribute() {
        super();
    }
    
    /**
     * Instantiates a new attribute.
     *
     * @param name
     *            the name
     */
    public Attribute(String name) {
        super();
        this.name = name;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description
     * 
     * @param description a description
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
