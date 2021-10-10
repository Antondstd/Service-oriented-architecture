import {Injectable} from '@angular/core';
import {Ticket, TicketType} from "./Model/ticket.model";
// @ts-ignore
import * as xml2js from 'xml2js';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Filter} from "./app.component";
import {FilterField} from "./app.component";
// @ts-ignore
import * as dateformat from 'dateformat';
import {TableAdditions} from "./Model/table-additions.model";
import * as moment from "moment";

@Injectable({
  providedIn: 'root'
})
export class TicketsService {

  constructor(private http: HttpClient) {
  }
  // SERVER_URL = "http://localhost:8080/api"
  // SERVER_URL = "http://localhost:2111"
  SERVER_URL = location.origin

  addTicket(ticket: Ticket): Observable<any> {
    var builder = new xml2js.Builder({'rootName': 'Ticket'});
    var bodyXML = builder.buildObject(ticket);
    const myHeaders = new HttpHeaders().set('Content-Type', 'text/xml;charset=utf-8');
    return this.http.post(this.SERVER_URL+"/tickets", bodyXML, {
      headers: myHeaders,
      observe: 'response',
      responseType: 'text'
    })

  }

  getTickets(additionsForTable: TableAdditions, sortState: Array<String>): Observable<String> {
    console.log(location.origin)
    const myHeaders = new HttpHeaders().set('Content-Type', 'text/xml;charset=utf-8');
    let currentPage = additionsForTable.currentPage
    if (currentPage < 1)
      currentPage = 1
    console.log("SORT STATE SIZE IS" + sortState.length)
    var params = new HttpParams().append('page', currentPage)
      .append('perPage', additionsForTable.perPage)
    if (sortState.length > 0)
      params = params.append('sortState', sortState.join(','));
    if (additionsForTable.filter.isActive) {
      additionsForTable.filter.fields.forEach((field: FilterField, key: String) => {
          // if (field.isActive && field.data != null && field.data != "")
          console.log("ADDING PARAM " + key + " " + field.isActive)
          if (field.isActive && field.formControl.value != null && field.formControl.touched && field.formControl.value.toString() != "") {
            console.log("ADDING PARAM " + key)
            console.log("DATE" + field.formControl.value.toString())
            if (key == "event.date" || key == "creationDate") {
              console.log("DATE " + field.formControl.value.toString())
              if (key == "event.date")
                params = params.append(key.toString(), dateformat(field.formControl.value.toString(), "HH:MM:ss dd/mm/yy"))
              else {

                let newDate = moment(field.formControl.value.toString()).format()
                  // let newDate =  dateformat(field.formControl.value.toString(), "dd/mm/yy")
                console.log("NEWDATE " + newDate)
                // let zone =  moment.tz.guess();
                params = params.append(key.toString(), newDate)
              }
            }
            else
              params = params.append(key.toString(), field.formControl.value.toString())
          }
        }
      )
    }
    return this.http.get(this.SERVER_URL+"/tickets", {
      headers: myHeaders,
      params: params,
      responseType: "text"
    })
  }

  updateTicket(ticket: Ticket): Observable<any> {
    var builder = new xml2js.Builder({'rootName': 'Ticket'});
    var bodyXML = builder.buildObject(ticket);
    const myHeaders = new HttpHeaders().set('Content-Type', 'text/xml;charset=utf-8');
    // var params = new HttpParams().append('id', ticket.id);
    return this.http.put(this.SERVER_URL+"/tickets/"+ticket.id, bodyXML, {
      headers: myHeaders,
      observe: 'response',
      responseType: "text"
    })
  }

  deleteTicket(id: number): Observable<any> {
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
    return this.http.delete(this.SERVER_URL+"/tickets/"+id, {
      headers: myHeaders,
      observe: 'response'
    })
  }

  deleteTicketByType(type: TicketType): Observable<any> {
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
    var params = new HttpParams().append('type', type);
    return this.http.delete(this.SERVER_URL+"/additions/deleteTypeTicket", {
      headers: myHeaders,
      params: params,
      observe: 'response',
      responseType: "text"
    })
  }

  sellVipTicket(id: Number): Observable<any> {
    const myHeaders = new HttpHeaders().set('Content-Type', 'text/xml;charset=utf-8');
    return this.http.post(this.SERVER_URL+"/booking/sell/vip/"+id,"", {
      headers: myHeaders,
      observe: 'response',
      responseType: "text"
    })
  }

  cancelEvent(id: Number): Observable<any> {
    const myHeaders = new HttpHeaders().set('Content-Type', 'text/xml;charset=utf-8');
    return this.http.delete(this.SERVER_URL+"/booking/event/"+id+"/cancel", {
      headers: myHeaders,
      observe: 'response',
      responseType: "text"
    })
  }

  getGroupedDiscount(): Observable<any> {
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
    return this.http.get(this.SERVER_URL+"/additions/groupedDiscountTicket", {
      headers: myHeaders,
      observe: 'response',
      responseType: "text"
    })
  }

  getDistinctType(): Observable<any> {
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
    return this.http.get(this.SERVER_URL+"/additions/distinctTypeTicket", {
      headers: myHeaders,
      observe: 'response',
      responseType: "text"
    })
  }
}
