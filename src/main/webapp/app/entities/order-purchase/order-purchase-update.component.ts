import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IOrderPurchase } from 'app/shared/model/order-purchase.model';
import { OrderPurchaseService } from './order-purchase.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
    selector: 'jhi-order-purchase-update',
    templateUrl: './order-purchase-update.component.html'
})
export class OrderPurchaseUpdateComponent implements OnInit {
    orderPurchase: IOrderPurchase;
    isSaving: boolean;

    customers: ICustomer[];
    orderDateDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected orderPurchaseService: OrderPurchaseService,
        protected customerService: CustomerService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ orderPurchase }) => {
            this.orderPurchase = orderPurchase;
        });
        this.customerService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICustomer[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICustomer[]>) => response.body)
            )
            .subscribe((res: ICustomer[]) => (this.customers = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.orderPurchase.id !== undefined) {
            this.subscribeToSaveResponse(this.orderPurchaseService.update(this.orderPurchase));
        } else {
            this.subscribeToSaveResponse(this.orderPurchaseService.create(this.orderPurchase));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderPurchase>>) {
        result.subscribe((res: HttpResponse<IOrderPurchase>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCustomerById(index: number, item: ICustomer) {
        return item.id;
    }
}
