export class GroupedDiscount {
  discount:number = 0
  count:number = 0

  constructor(obj:any) {
    obj && Object.assign(this,obj)
  }
}
