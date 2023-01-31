import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { expect } from '@jest/globals';
import { RouterTestingModule } from '@angular/router/testing';
import { UserService } from 'src/app/services/user.service';
import { Observable, of } from 'rxjs';
import { Router } from '@angular/router';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let sessionService: SessionService;
  let userService: UserService;
  let router: Router;
  let matSnackBar: MatSnackBar;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        RouterTestingModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [SessionService, UserService],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    sessionService.sessionInformation = {token: '', type: '', id: 0, username: '', firstName: '', lastName: '', admin: false};
    userService = TestBed.inject(UserService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should delete', () => {
    const deleteSpy = jest.spyOn(userService, 'delete').mockReturnValue(of('test'));
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
    const logoutSpy = jest.spyOn(sessionService, 'logOut').mockImplementation(()=>{});
    const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation(() => new Promise<boolean>((resolve, _) => resolve(true)));
    component.delete();
    expect(deleteSpy).toHaveBeenCalledTimes(1);
    expect(snackBarSpy).toHaveBeenCalledTimes(1);
    expect(logoutSpy).toHaveBeenCalledTimes(1);
    expect(navigateSpy).toHaveBeenCalledTimes(1);
  });
});
