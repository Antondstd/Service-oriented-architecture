import {ChangeDetectorRef, Component, Inject, ViewChild} from '@angular/core';
import {MatCalendarCellClassFunction} from '@angular/material/datepicker';
import {EventType} from "./Model/event.model";
import {Ticket, TicketType} from "./Model/ticket.model";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
// @ts-ignore
import * as xml2js from 'xml2js';
import * as moment from 'moment';
// @ts-ignore
import * as dateformat from 'dateformat';
import {TicketsService} from "./tickets.service";
import {MatTable} from "@angular/material/table";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatSort, Sort} from "@angular/material/sort";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {TableAdditions} from "./Model/table-additions.model";
import {FormControl} from "@angular/forms";
import {GroupedDiscount} from "./Model/grouped-discount.model";
import {HttpErrorResponse} from "@angular/common/http";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  SNACK_BAR_DURATION = 4 * 1000
  filter: Filter = new Filter()
  title = 'SOA_Lab1'
  tickets: Array<Ticket> = []
  additionForTable = new TableAdditions()
  sortStateArray: Array<String> = []
  sortedState: Map<String, String> = new Map()
  deleteTypeTicket:TicketType = TicketType.USUAL
  // @ts-ignore
  typeRanges = Object.values(TicketType)
  selectedTypeRange = this.typeRanges[0]
  // @ts-ignore
  eventTypeRanges = Object.values(EventType)
  selectedEventTypeRange = this.eventTypeRanges[0]

  @ViewChild(MatTable, {static: false})
  ticketsTable!: MatTable<any>

  @ViewChild(MatPaginator) paginator1!: MatPaginator;

  @ViewChild(MatSort) sort!: MatSort;

  applyFilter($event: KeyboardEvent) {

  }

  constructor(public dialog: MatDialog,
              private ticketService: TicketsService,
              private changeDetectorRefs: ChangeDetectorRef,
              private _snackBar: MatSnackBar) {
    this.getTickets()
  }

  getTickets() {
    this.ticketService.getTickets(this.additionForTable, this.sortStateArray).toPromise().then((response: String) => { //Длинный список параметров
      xml2js.parseString(response, {explicitArray: false}, (error: string | undefined, result: any) => {
        if (error) {
          console.log(error)
        } else {
          let responsePagesTickets = result.ResponsePagesTickets
          if (this.additionForTable.currentPage > responsePagesTickets.currentPage) {
            this.paginator1.pageIndex = responsePagesTickets.currentPage - 1
          }
          this.additionForTable.currentPage = responsePagesTickets.currentPage
          this.additionForTable.lastPage = responsePagesTickets.lastPage
          this.additionForTable.countAll = responsePagesTickets.countAll
          if (this.additionForTable.countAll == 0)
            this.tickets = []
          else if (responsePagesTickets.tickets != "") {
            if (responsePagesTickets.tickets.Ticket.constructor == Array) {
              this.tickets = responsePagesTickets.tickets.Ticket
            } else {
              let ticket = new Ticket(responsePagesTickets.tickets.Ticket)
              this.tickets = [ticket]
            }
          } else
            this.tickets = []
          this.ticketsTable.renderRows()
        }
      });
    }).catch((response:any) => this.handleResponse(response))
  }

  deleteTicket(position: number) {
    let curId = this.tickets[position].id
    this.ticketService.deleteTicket(this.tickets[position].id).toPromise().then((data: Response) => {
        console.log(data)
        if (data.ok){
          this.openSnackBarMessage("Успешно удален объект")
          this.getTickets()
        }
        //   console.log("DATA OK")
        // if (data.ok && curId == this.tickets[position].id) {
        //   this.tickets.splice(position, 1)
        //   console.log("SPLICED")
        //
        // }
        // console.log(this.sort.sortables.get("id"))
      }
    ).catch((response:any) => this.handleResponse(response))
  }

  deleteTicketByType(type:TicketType){
    this.ticketService.deleteTicketByType(type).toPromise().then((data:Response) => {
      if (data.ok){
        this.openSnackBarMessage("Успешно удален Ticket с ID " + data.body)
        this.getTickets()
      }
      else{
        this.openSnackBarMessage("Ошибка: " + data.status + "\n" +
          "Сообщение: " + data.body)
      }
    }).catch((response:any) => this.handleResponse(response))
  }

  openAddTicketDialog(isNew: boolean, position: number): void {
    let ticket = new Ticket(Object())
    if (!isNew)
      ticket = this.tickets[position]
    const dialogRef = this.dialog.open(AddTicketDialog, {
      width: '450px',
      data: {isNew: isNew, ticket: ticket, message: ""}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result)
        console.log("After dialog get tickets")
      this.getTickets()
    });
  }

  getGroupedDiscount() {
    this.ticketService.getGroupedDiscount().toPromise().then((response: Response) => {

      if (response.ok) {
        xml2js.parseString(response.body, {explicitArray: false}, (error: string | undefined, result: any) => {
          if (error) {
            console.log(error)
          } else {
            console.log(result)
            let listGrouped = result.list
            console.log(listGrouped)
            if (listGrouped != "") {
              if (listGrouped.GroupedDiscount.constructor == Array) {
                console.log(listGrouped.GroupedDiscount as Array<GroupedDiscount>)
                const dialogRef = this.dialog.open(AdditionsTable, {
                  width: '450px',
                  data: listGrouped.GroupedDiscount as Array<GroupedDiscount>
                });
              } else {
                let oneGroupedDiscount = new GroupedDiscount(listGrouped.GroupedDiscount)
                listGrouped = [oneGroupedDiscount]
                const dialogRef = this.dialog.open(AdditionsTable, {
                  width: '450px',
                  data: listGrouped as Array<GroupedDiscount>
                });
                // let ticket = new Ticket(responsePagesTickets.tickets.Ticket)
                // this.tickets = [ticket]
              }
            } else
              this.openSnackBarMessage("Нету ни одного элемента с discount")
          }
        });
      }

    }).catch((response:any) => this.handleResponse(response))
  }

  getDistinctType(){
    this.ticketService.getDistinctType().toPromise( ).then((response:Response) => {
      let list
      let message = ""
      if (response.ok){
        xml2js.parseString(response.body, {explicitArray: false}, (error: string | undefined, result: any) => {
          if (error) {
            console.log(error)
          } else {
            console.log(result)
            let listGrouped = result.ResponseAdditions.listOfDistictTicketType
            console.log(listGrouped)
            if (listGrouped != "") {
              if (listGrouped.TicketType.constructor == Array) {
                console.log(listGrouped.TicketType as Array<TicketType>)
                list = listGrouped.TicketType as Array<TicketType>
              } else {
                list = listGrouped.TicketType
                // let ticket = new Ticket(responsePagesTickets.tickets.Ticket)
                // this.tickets = [ticket]
              }
            } else
              this.openSnackBarMessage("Нету ни одного элемента с discount")
          }
        });
        this.openSnackBarMessage("Список уникальны типов: " + list)
      }
    }).catch((response:any) => this.handleResponse(response))
  }

  dateClass: MatCalendarCellClassFunction<Date> = (cellDate, view) => {
    // Only highligh dates inside the month view.
    if (view === 'month') {
      const date = cellDate.getDate()

      // Highlight the 1st and 20th day of each month.
      return (date === 1 || date === 20) ? 'example-custom-date-class' : ''
    }

    return ''
  }

  openSnackBarMessage(message: string) {
    this._snackBar.open(message, "close", {
      duration: this.SNACK_BAR_DURATION,
      horizontalPosition: 'left',
      verticalPosition: 'bottom',
      panelClass: ['snackbar']
    });
  }

  sortData(sort: Sort) {
    if (sort.direction == "")
      this.sortedState.delete(sort.active)
    else if (!this.sortedState.has(sort.active)) {
      this.sortedState.set(sort.active, sort.direction)
    } else {
      this.sortedState.delete(sort.active)
      this.sortedState.set(sort.active, sort.direction)
    }
    this.sortStateArray.length = 0
    this.sortedState.forEach((value: String, key: String) =>
      this.sortStateArray.unshift(value + "(" + key + ")")
    )
    this.getTickets()
    console.log(this.sortStateArray)
  }

  paginator(event: PageEvent) {
    this.additionForTable.currentPage = event.pageIndex + 1
    console.log("NOW PageIndex is " + event.pageIndex)
    this.getTickets()
  }

  handleResponse(response:any){
    if (!response.ok)
      this.openSnackBarMessage("Ошибка: "+ response.status + "\n" +
        "Сообщение: " +response.error)
  }
}

@Component({
  selector: 'add.ticket.dialog',
  templateUrl: 'add.ticket.dialog.html', styleUrls: ['./app.component.css']
})
export class AddTicketDialog {
  ticket: Ticket = new Ticket(Object())
  typeRanges = Object.values(TicketType)
  eventTypeRanges = Object.values(EventType)
  isNew: Boolean = true
  curDate: Date = new Date()
  // eventDate: Date = new Date()
  eventDate = new Date()
  parser = new xml2js.Parser();
  builder = new xml2js.Builder()
  SNACK_BAR_DURATION = 4 * 1000

  constructor(
    public dialogRef: MatDialogRef<AddTicketDialog>,
    private ticketService: TicketsService,
    private _snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
    this.ticket = new Ticket(data.ticket)
    this.isNew = data.isNew
    if (!this.isNew) {
      // this.eventDate = new Date(moment(data.ticket.event.date.toString(),'H:m:s dd/MM/yy',true).format())
      let mom = moment(data.ticket.event.date.toString(), 'HH:mm:ss DD/MM/YY z', true).format("YYYY-MM-DDTHH:mm z")
      // this.eventDate = new Date(mom)
    }
  }

  upload(): void {
    console.log(this.ticket)
    // console.log(this.builder.build(this.ticket))
    var builder = new xml2js.Builder({'rootName': 'Ticket'});
    this.ticket.setCreationDateFromDate(this.curDate)
    this.ticket.event.setDateFromDate(this.eventDate)
    var xml = builder.buildObject(this.ticket);
    console.log(xml)
      this.ticketService.addTicket(this.ticket).toPromise().then((response: Response) => {
        console.log(response)
        this.handleResponse(response)
      }).catch(error => this.handleResponse(error))

  }

  handleResponse(response:any){
    if (response.ok)
      this.showMessageAndCloseDialog(response.ok)
    else
      this.openSnackBarMessage("Ошибка: "+ response.status + "\n" +
        "Сообщение: " +response.error)
  }

  showMessageAndCloseDialog(responseAnswer: Boolean) {
    if (responseAnswer) {
      this.openSnackBarMessage("Successfully added new Ticket")
      this.dialogRef.close(true);
    } else {
      this.openSnackBarMessage("Wasn't able to add the Ticket :(")
      this.dialogRef.close(false);
    }
  }

  update(): void {
    var builder = new xml2js.Builder({'rootName': 'Ticket'});
    this.ticket.setCreationDateFromDate(this.curDate)
    this.ticket.event.setDateFromDate(this.eventDate)
    var xml = builder.buildObject(this.ticket);
    console.log(xml)
    this.ticketService.updateTicket(this.ticket).toPromise().then((response: Response) => {
      this.handleResponse(response)
    })
  }

  openSnackBarMessage(message: string) {
    this._snackBar.open(message, "close", {
      duration: this.SNACK_BAR_DURATION,
      horizontalPosition: 'left',
      verticalPosition: 'bottom',
      panelClass: ['snackbar']
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  selector: 'additional.table.ticket.dialog',
  templateUrl: 'additional.table.ticket.dialog.html', styleUrls: ['./app.component.css']
})
export class AdditionsTable {
  listOfGroupedDiscount:Array<GroupedDiscount> = []
  columns = ['discount','count']

  constructor(
    public dialogRef: MatDialogRef<AddTicketDialog>,
    private ticketService: TicketsService,
    private _snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: Array<GroupedDiscount>) {
    this.listOfGroupedDiscount = data
    console.log(this.listOfGroupedDiscount)
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}

export interface DialogData {
  isNew: boolean;
  ticket: Ticket;
  message: String;
}

export class Filter {
  fields: Map<String, FilterField> = new Map<String, FilterField>()
  isActive: Boolean = true
}

export class FilterField {
  data: any = null
  formControl: FormControl = new FormControl()
  isActive: Boolean = true
}

