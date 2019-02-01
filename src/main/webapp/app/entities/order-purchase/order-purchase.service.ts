import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrderPurchase } from 'app/shared/model/order-purchase.model';

type EntityResponseType = HttpResponse<IOrderPurchase>;
type EntityArrayResponseType = HttpResponse<IOrderPurchase[]>;

@Injectable({ providedIn: 'root' })
export class OrderPurchaseService {
    public resourceUrl = SERVER_API_URL + 'api/order-purchases';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/order-purchases';

    constructor(protected http: HttpClient) {}

    create(orderPurchase: IOrderPurchase): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(orderPurchase);
        return this.http
            .post<IOrderPurchase>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(orderPurchase: IOrderPurchase): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(orderPurchase);
        return this.http
            .put<IOrderPurchase>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IOrderPurchase>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrderPurchase[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrderPurchase[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(orderPurchase: IOrderPurchase): IOrderPurchase {
        const copy: IOrderPurchase = Object.assign({}, orderPurchase, {
            orderDate:
                orderPurchase.orderDate != null && orderPurchase.orderDate.isValid() ? orderPurchase.orderDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.orderDate = res.body.orderDate != null ? moment(res.body.orderDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((orderPurchase: IOrderPurchase) => {
                orderPurchase.orderDate = orderPurchase.orderDate != null ? moment(orderPurchase.orderDate) : null;
            });
        }
        return res;
    }
}
