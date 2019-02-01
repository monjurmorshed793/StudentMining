import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderPurchase } from 'app/shared/model/order-purchase.model';

@Component({
    selector: 'jhi-order-purchase-detail',
    templateUrl: './order-purchase-detail.component.html'
})
export class OrderPurchaseDetailComponent implements OnInit {
    orderPurchase: IOrderPurchase;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orderPurchase }) => {
            this.orderPurchase = orderPurchase;
        });
    }

    previousState() {
        window.history.back();
    }
}
