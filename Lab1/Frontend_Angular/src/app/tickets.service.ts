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

@Injectable({
  providedIn: 'root'
})
export class TicketsService {

  constructor(private http: HttpClient) {
  }
  SERVER_URL = "http://localhost:8080"
  // SERVER_URL = "http://localhost:8080/"

  addTicket(ticket: Ticket): Observable<any> {
    var builder = new xml2js.Builder({'rootName': 'Ticket'});
    var bodyXML = builder.buildObject(ticket);
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
    return this.http.post(this.SERVER_URL+"/api/ticket", bodyXML, {
      headers: myHeaders,
      observe: 'response',
      responseType: 'text'
    })

  }

  getTickets(additionsForTable: TableAdditions, sortState: Array<String>): Observable<String> { //длинный список параметров
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
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
          if (field.isActive && field.formControl.touched && field.formControl.value.toString() != "") {
            console.log("ADDING PARAM " + key)
            if (key == "event.date" || key == "creationDate")
              if (key == "event.date")
                params = params.append(key.toString(), dateformat(field.formControl.value.toString(), "HH:MM:ss dd/mm/yy"))
              else
                params = params.append(key.toString(), dateformat(field.formControl.value.toString(), "HH:MM:ss dd/mm/yy z"))
            else
              params = params.append(key.toString(), field.formControl.value.toString())
          }
        }
      )
    }
    return this.http.get(this.SERVER_URL+"/api/ticket", {
      headers: myHeaders,
      params: params,
      responseType: "text"
    })
  }

  updateTicket(ticket: Ticket): Observable<any> {
    var builder = new xml2js.Builder({'rootName': 'Ticket'});
    var bodyXML = builder.buildObject(ticket);
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
    // var params = new HttpParams().append('id', ticket.id);
    return this.http.put(this.SERVER_URL+"/api/ticket/"+ticket.id, bodyXML, {
      headers: myHeaders,
      observe: 'response',
      responseType: "text"
    })
  }

  deleteTicket(id: number): Observable<any> {
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
    return this.http.delete(this.SERVER_URL+"/api/ticket/"+id, {
      headers: myHeaders,
      observe: 'response'
    })
  }

  deleteTicketByType(type: TicketType): Observable<any> {
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
    var params = new HttpParams().append('type', type);
    return this.http.delete(this.SERVER_URL+"/api/additions/deleteTypeTicket", {
      headers: myHeaders,
      params: params,
      observe: 'response',
      responseType: "text"
    })
  }

  getGroupedDiscount(): Observable<any> {
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
    return this.http.get(this.SERVER_URL+"/api/additions/groupedDiscountTicket", {
      headers: myHeaders,
      observe: 'response',
      responseType: "text"
    })
  }

  getDistinctType(): Observable<any> {
    const myHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
    return this.http.get(this.SERVER_URL+"/api/additions/distinctTypeTicket", {
      headers: myHeaders,
      observe: 'response',
      responseType: "text"
    })
  }
}
