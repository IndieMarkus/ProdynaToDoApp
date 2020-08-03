package at.markus.lehr.prodynatodo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link at.markus.lehr.prodynatodo.domain.ToDoEntry} entity. This class is used
 * in {@link at.markus.lehr.prodynatodo.web.rest.ToDoEntryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /to-do-entries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ToDoEntryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private BooleanFilter published;

    private InstantFilter dueDate;

    private BooleanFilter done;

    private LongFilter creatorId;

    public ToDoEntryCriteria() {
    }

    public ToDoEntryCriteria(ToDoEntryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.published = other.published == null ? null : other.published.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.done = other.done == null ? null : other.done.copy();
        this.creatorId = other.creatorId == null ? null : other.creatorId.copy();
    }

    @Override
    public ToDoEntryCriteria copy() {
        return new ToDoEntryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getPublished() {
        return published;
    }

    public void setPublished(BooleanFilter published) {
        this.published = published;
    }

    public InstantFilter getDueDate() {
        return dueDate;
    }

    public void setDueDate(InstantFilter dueDate) {
        this.dueDate = dueDate;
    }

    public BooleanFilter getDone() {
        return done;
    }

    public void setDone(BooleanFilter done) {
        this.done = done;
    }

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ToDoEntryCriteria that = (ToDoEntryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(published, that.published) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(done, that.done) &&
            Objects.equals(creatorId, that.creatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        description,
        published,
        dueDate,
        done,
        creatorId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToDoEntryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (published != null ? "published=" + published + ", " : "") +
                (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
                (done != null ? "done=" + done + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
            "}";
    }

}
