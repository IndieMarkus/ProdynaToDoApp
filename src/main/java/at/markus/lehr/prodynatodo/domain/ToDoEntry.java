package at.markus.lehr.prodynatodo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ToDoEntry.
 */
@Entity
@Table(name = "to_do_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ToDoEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "done")
    private Boolean done;

    @ManyToOne
    @JsonIgnoreProperties(value = "toDoEntries", allowSetters = true)
    private User creator;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ToDoEntry title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public ToDoEntry description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isPublished() {
        return published;
    }

    public ToDoEntry published(Boolean published) {
        this.published = published;
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public ToDoEntry dueDate(Instant dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean isDone() {
        return done;
    }

    public ToDoEntry done(Boolean done) {
        this.done = done;
        return this;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public User getCreator() {
        return creator;
    }

    public ToDoEntry creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToDoEntry)) {
            return false;
        }
        return id != null && id.equals(((ToDoEntry) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToDoEntry{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", published='" + isPublished() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", done='" + isDone() + "'" +
            "}";
    }
}
