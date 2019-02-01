/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudentMiningTestModule } from '../../../test.module';
import { OrderPurchaseDetailComponent } from 'app/entities/order-purchase/order-purchase-detail.component';
import { OrderPurchase } from 'app/shared/model/order-purchase.model';

describe('Component Tests', () => {
    describe('OrderPurchase Management Detail Component', () => {
        let comp: OrderPurchaseDetailComponent;
        let fixture: ComponentFixture<OrderPurchaseDetailComponent>;
        const route = ({ data: of({ orderPurchase: new OrderPurchase(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [StudentMiningTestModule],
                declarations: [OrderPurchaseDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OrderPurchaseDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrderPurchaseDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.orderPurchase).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
