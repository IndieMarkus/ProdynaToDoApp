import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'to-do-entry',
        loadChildren: () => import('./to-do-entry/to-do-entry.module').then(m => m.ProdynaToDoAppToDoEntryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class ProdynaToDoAppEntityModule {}
