import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrderPurchase } from 'app/shared/model/order-purchase.model';
import { OrderPurchaseService } from './order-purchase.service';

@Component({
    selector: 'jhi-order-purchase-delete-dialog',
    templateUrl: './order-purchase-delete-dialog.component.html'
})
export class OrderPurchaseDeleteDialogComponent {
    orderPurchase: IOrderPurchase;

    constructor(
        protected orderPurchaseService: OrderPurchaseService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.orderPurchaseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'orderPurchaseListModification',
                content: 'Deleted an orderPurchase'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-order-purchase-delete-popup',
    template: ''
})
export class OrderPurchaseDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ orderPurchase }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OrderPurchaseDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.orderPurchase = orderPurchase;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/order-purchase', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/order-purchase', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
