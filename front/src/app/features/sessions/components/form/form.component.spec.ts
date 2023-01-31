import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { SessionService } from 'src/app/services/session.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';

class MatSnackBarStub {
  open() {
    return {
      onAction: () => of({})
    }
  }
}

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
  let router: Router;
  let matSnackBar: MatSnackBar;
  const sessionInfos: SessionInformation = { username: "", firstName: "", lastName: "", id: 0, admin: false, token: "", type: "" };

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        SessionService,
        SessionApiService,
        {provide: MatSnackBar, useClass: MatSnackBarStub}
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    sessionService.sessionInformation = sessionInfos;
    sessionApiService = TestBed.inject(SessionApiService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create session', () => {
    const createSpy = jest.spyOn(sessionApiService, 'create').mockImplementation((session: Session) => of(session));
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
    const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation(() => new Promise<boolean>((resolve, _) => resolve(true)));
    component.onUpdate = false;
    component.submit();
    expect(createSpy).toHaveBeenCalledTimes(1);
    expect(snackBarSpy).toHaveBeenCalledTimes(1);
    expect(navigateSpy).toHaveBeenCalledTimes(1);
  });

  it('should update session', () => {
    const updateSpy = jest.spyOn(sessionApiService, 'update').mockImplementation((_, session: Session) => of(session));
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
    const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation(() => new Promise<boolean>((resolve, _) => resolve(true)));
    component.onUpdate = true;
    component.submit();
    expect(updateSpy).toHaveBeenCalledTimes(1);
    expect(snackBarSpy).toHaveBeenCalledTimes(1);
    expect(navigateSpy).toHaveBeenCalledTimes(1);
  });

});
