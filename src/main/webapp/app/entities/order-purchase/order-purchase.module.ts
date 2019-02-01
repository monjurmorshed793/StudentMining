import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StudentMiningSharedModule } from 'app/shared';
import {
    OrderPurchaseComponent,
    OrderPurchaseDetailComponent,
    OrderPurchaseUpdateComponent,
    OrderPurchaseDeletePopupComponent,
    OrderPurchaseDeleteDialogComponent,
    orderPurchaseRoute,
    orderPurchasePopupRoute
} from './';

const ENTITY_STATES = [...orderPurchaseRoute, ...orderPurchasePopupRoute];

@NgModule({
    imports: [StudentMiningSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrderPurchaseComponent,
        OrderPurchaseDetailComponent,
        OrderPurchaseUpdateComponent,
        OrderPurchaseDeleteDialogComponent,
        OrderPurchaseDeletePopupComponent
    ],
    entryComponents: [
        OrderPurchaseComponent,
        OrderPurchaseUpdateComponent,
        OrderPurchaseDeleteDialogComponent,
        OrderPurchaseDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StudentMiningOrderPurchaseModule {}
