/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { StudentMiningTestModule } from '../../../test.module';
import { OrderPurchaseUpdateComponent } from 'app/entities/order-purchase/order-purchase-update.component';
import { OrderPurchaseService } from 'app/entities/order-purchase/order-purchase.service';
import { OrderPurchase } from 'app/shared/model/order-purchase.model';

describe('Component Tests', () => {
    describe('OrderPurchase Management Update Component', () => {
        let comp: OrderPurchaseUpdateComponent;
        let fixture: ComponentFixture<OrderPurchaseUpdateComponent>;
        let service: OrderPurchaseService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [StudentMiningTestModule],
                declarations: [OrderPurchaseUpdateComponent]
            })
                .overrideTemplate(OrderPurchaseUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OrderPurchaseUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrderPurchaseService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new OrderPurchase(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.orderPurchase = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new OrderPurchase();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.orderPurchase = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
