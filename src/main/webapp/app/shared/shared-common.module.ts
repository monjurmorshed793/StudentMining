import { NgModule } from '@angular/core';

import { StudentMiningSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [StudentMiningSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [StudentMiningSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class StudentMiningSharedCommonModule {}
