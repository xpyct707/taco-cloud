import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { routes } from './app.routes';

import { CloudTitleComponent } from './cloud-title/cloud-title.component';
import { DesignComponent } from './design/design.component';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { HomeComponent } from './home/home.component';
import { LittleButtonComponent } from './little-button/little-button.component';
import { NonWrapsPipe } from './recents/non-wraps.pipe';
import { RecentTacosComponent } from './recents/recents.component';
import { WrapsPipe } from './recents/wraps.pipe';
import { CartComponent } from './cart/cart.component';
import { BigButtonComponent } from './big-button/big-button.component';
import { GroupBoxComponent } from './group-box/group-box.component';
import { ApiService } from './api/api.service';
import { CartService } from './cart/cart.service';
import { RecentTacosService } from './recents/recent-tacos.service';
import { LoginComponent } from './login/login.component';


@NgModule({
  declarations: [
    AppComponent,
    CloudTitleComponent,
    DesignComponent,
    FooterComponent,
    HomeComponent,
    HeaderComponent,
    LittleButtonComponent,
    NonWrapsPipe,
    RecentTacosComponent,
    WrapsPipe,
    CartComponent,
    BigButtonComponent,
    GroupBoxComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    HttpModule,
    RouterModule.forRoot(routes)
  ],
  providers: [
    ApiService,
    CartService,
    RecentTacosService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
