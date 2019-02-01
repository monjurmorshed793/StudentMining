/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { StudentMiningTestModule } from '../../../test.module';
import { OrderPurchaseDeleteDialogComponent } from 'app/entities/order-purchase/order-purchase-delete-dialog.component';
import { OrderPurchaseService } from 'app/entities/order-purchase/order-purchase.service';

describe('Component Tests', () => {
    describe('OrderPurchase Management Delete Component', () => {
        let comp: OrderPurchaseDeleteDialogComponent;
        let fixture: ComponentFixture<OrderPurchaseDeleteDialogComponent>;
        let service: OrderPurchaseService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [StudentMiningTestModule],
                declarations: [OrderPurchaseDeleteDialogComponent]
            })
                .overrideTemplate(OrderPurchaseDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrderPurchaseDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrderPurchaseService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
