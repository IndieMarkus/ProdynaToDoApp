import { Moment } from 'moment';

export interface IToDoEntry {
  id?: number;
  title?: string;
  description?: string;
  published?: boolean;
  dueDate?: Moment;
  done?: boolean;
  creatorId?: number;
}

export class ToDoEntry implements IToDoEntry {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string,
    public published?: boolean,
    public dueDate?: Moment,
    public done?: boolean,
    public creatorId?: number
  ) {
    this.published = this.published || false;
    this.done = this.done || false;
  }
}
