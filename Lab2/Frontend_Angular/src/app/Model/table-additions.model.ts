import {Filter, FilterField} from "../app.component";
import {filter} from "rxjs/operators";

export class TableAdditions {
  public currentPage: number = 1
  public lastPage: number = 1
  public perPage: number = 5
  public countAll: number = 1
  public columnsForFiltering: string[] = ['id',
    'name',
    'coordinates.x',
    'coordinates.y',
    'creationDate',
    'price',
    'discount',
    'comment',
    'type',
    'event.id',
    'event.name',
    'event.date',
    'event.minAge']
  public columnsShow: string[] = ['edit']
  public filter: Filter = new Filter()

  public numberPattern: String = "-?\d*"

  getAllTableFields() {
    return this.columnsForFiltering.concat(this.columnsShow)
  }

  getField(fieldName: String) {
    return this.filter.fields.get(fieldName)
  }

  constructor() {
    this.columnsForFiltering.forEach(value => {
      console.log("FILTER COLUMN " + value)
      this.filter.fields.set(value, new FilterField())
    })
    console.log("FILTER " + this.filter.fields.values())
  }
}
