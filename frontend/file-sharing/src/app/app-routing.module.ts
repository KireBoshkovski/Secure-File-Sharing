import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AuthGuard } from './guards/auth.guard';
import { CreatedFilesComponent } from './components/created-files/created-files.component';
import { SharedFilesComponent } from './components/shared-files/shared-files.component';
import { UploadComponent } from './components/upload/upload.component';
import { EditTextFileComponent } from './components/edit-text-file/edit-text-file.component';
import { VerifyUserComponent } from './components/verify-user/verify-user.component';
import { VerifyOtpComponent } from './components/verify-otp/verify-otp.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'verify-email', component: VerifyUserComponent },
  { path: 'verify-otp', component: VerifyOtpComponent},
  { path: 'created-files', component: CreatedFilesComponent, canActivate: [AuthGuard] },
  { path: 'shared-files', component: SharedFilesComponent, canActivate: [AuthGuard] },
  { path: 'upload-file', component: UploadComponent, canActivate: [AuthGuard] },
  { path: 'edit', component: EditTextFileComponent, canActivate: [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
