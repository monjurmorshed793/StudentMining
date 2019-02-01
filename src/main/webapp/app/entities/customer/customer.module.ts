import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StudentMiningSharedModule } from 'app/shared';
import {
    CustomerComponent,
    CustomerDetailComponent,
    CustomerUpdateComponent,
    CustomerDeletePopupComponent,
    CustomerDeleteDialogComponent,
    customerRoute,
    customerPopupRoute
} from './';
import { FileSelectDirective } from 'ng2-file-upload';
import { AngularFileUploaderModule } from 'angular-file-uploader';

const ENTITY_STATES = [...customerRoute, ...customerPopupRoute];

@NgModule({
    imports: [StudentMiningSharedModule, AngularFileUploaderModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CustomerComponent,
        CustomerDetailComponent,
        CustomerUpdateComponent,
        CustomerDeleteDialogComponent,
        CustomerDeletePopupComponent
    ],
    entryComponents: [CustomerComponent, CustomerUpdateComponent, CustomerDeleteDialogComponent, CustomerDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StudentMiningCustomerModule {}
