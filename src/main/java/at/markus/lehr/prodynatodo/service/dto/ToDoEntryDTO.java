package at.markus.lehr.prodynatodo.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link at.markus.lehr.prodynatodo.domain.ToDoEntry} entity.
 */
public class ToDoEntryDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;

    private String description;

    private Boolean published;

    private Instant dueDate;

    private Boolean done;


    private Long creatorId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long userId) {
        this.creatorId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToDoEntryDTO)) {
            return false;
        }

        return id != null && id.equals(((ToDoEntryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToDoEntryDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", published='" + isPublished() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", done='" + isDone() + "'" +
            ", creatorId=" + getCreatorId() +
            "}";
    }
}
