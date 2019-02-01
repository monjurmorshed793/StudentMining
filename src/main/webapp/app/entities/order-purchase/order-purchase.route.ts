import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { OrderPurchase } from 'app/shared/model/order-purchase.model';
import { OrderPurchaseService } from './order-purchase.service';
import { OrderPurchaseComponent } from './order-purchase.component';
import { OrderPurchaseDetailComponent } from './order-purchase-detail.component';
import { OrderPurchaseUpdateComponent } from './order-purchase-update.component';
import { OrderPurchaseDeletePopupComponent } from './order-purchase-delete-dialog.component';
import { IOrderPurchase } from 'app/shared/model/order-purchase.model';

@Injectable({ providedIn: 'root' })
export class OrderPurchaseResolve implements Resolve<IOrderPurchase> {
    constructor(private service: OrderPurchaseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IOrderPurchase> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<OrderPurchase>) => response.ok),
                map((orderPurchase: HttpResponse<OrderPurchase>) => orderPurchase.body)
            );
        }
        return of(new OrderPurchase());
    }
}

export const orderPurchaseRoute: Routes = [
    {
        path: '',
        component: OrderPurchaseComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'OrderPurchases'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: OrderPurchaseDetailComponent,
        resolve: {
            orderPurchase: OrderPurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'OrderPurchases'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: OrderPurchaseUpdateComponent,
        resolve: {
            orderPurchase: OrderPurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'OrderPurchases'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: OrderPurchaseUpdateComponent,
        resolve: {
            orderPurchase: OrderPurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'OrderPurchases'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const orderPurchasePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: OrderPurchaseDeletePopupComponent,
        resolve: {
            orderPurchase: OrderPurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'OrderPurchases'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
