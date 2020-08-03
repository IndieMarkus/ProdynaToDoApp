import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ProdynaToDoAppSharedModule } from 'app/shared/shared.module';
import { ProdynaToDoAppCoreModule } from 'app/core/core.module';
import { ProdynaToDoAppAppRoutingModule } from './app-routing.module';
import { ProdynaToDoAppHomeModule } from './home/home.module';
import { ProdynaToDoAppEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    ProdynaToDoAppSharedModule,
    ProdynaToDoAppCoreModule,
    ProdynaToDoAppHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ProdynaToDoAppEntityModule,
    ProdynaToDoAppAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class ProdynaToDoAppAppModule {}
