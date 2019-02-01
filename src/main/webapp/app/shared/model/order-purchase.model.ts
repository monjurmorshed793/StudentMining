import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model/customer.model';

export interface IOrderPurchase {
    id?: number;
    orderDate?: Moment;
    orderAmount?: number;
    customer?: ICustomer;
}

export class OrderPurchase implements IOrderPurchase {
    constructor(public id?: number, public orderDate?: Moment, public orderAmount?: number, public customer?: ICustomer) {}
}
