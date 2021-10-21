import { Injectable } from "@angular/core";
import { Adapter } from "../models/adapter.model";

@Injectable({
    providedIn: "root",
})
export class DateAdapter implements Adapter<Date> {
    adapt(item: any): Date {
        const dtoDate = new Date(item);
        if (!dtoDate)
            return new Date(0,0,0,0,0,0,0);
        return new Date(
            Date.UTC(
                dtoDate.getFullYear(), dtoDate.getMonth(), dtoDate.getDate(), 
                dtoDate.getHours(), dtoDate.getMinutes(), dtoDate.getSeconds()
            ));
    }
}
  