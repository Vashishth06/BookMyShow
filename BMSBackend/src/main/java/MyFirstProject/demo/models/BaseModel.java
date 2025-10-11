package MyFirstProject.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Base class for all entity models in the application.
 * Provides common fields that every entity needs: id, createdAt, updatedAt.
 *
 * Benefits of inheritance:
 * - DRY principle: Don't repeat id, timestamps in every entity
 * - Consistency: All entities have same structure for common fields
 * - Easy maintenance: Changes to common fields happen in one place
 *
 * JPA Annotations:
 * @MappedSuperclass: Indicates this is not a table but a superclass for entities
 * @EntityListeners: Enables automatic auditing (auto-populate createdAt, updatedAt)
 *
 * All entities (User, Booking, Show, etc.) extend this class to inherit these fields.
 */
@Getter
@Setter
@MappedSuperclass  // Not a table itself, but provides fields to child entities
@EntityListeners(AuditingEntityListener.class)  // Enables JPA auditing
public class BaseModel {

    /**
     * Primary key for the entity.
     *
     * @Id: Marks this field as the primary key
     * @GeneratedValue: Auto-generates values for this field
     * IDENTITY strategy: Database auto-increments the value (1, 2, 3, ...)
     *
     * Every entity (User, Booking, Show) will have this id field.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Timestamp when the entity was first created.
     *
     * @CreatedDate: JPA automatically sets this when entity is first saved
     * Never updated after initial creation
     * Useful for tracking when records were created
     */
    @CreatedDate
    private Date createdAt;

    /**
     * Timestamp when the entity was last modified.
     *
     * @LastModifiedDate: JPA automatically updates this on every save operation
     * Useful for tracking when records were last changed
     * Can be used for caching, version control, audit trails
     */
    @LastModifiedDate
    private Date updatedAt;
}