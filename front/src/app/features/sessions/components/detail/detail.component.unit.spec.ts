import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { DetailComponent } from './detail.component';

class MatSnackBarStub {
  open() {
    return {
      onAction: () => of({})
    }
  }
}

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
  let matSnackBar: MatSnackBar;
  let router: Router;
  const sessionInfos: SessionInformation =
    { username: "", firstName: "", lastName: "", id: 0, admin: false, token: "", type: "" };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [
        SessionService,
        SessionApiService,
        { provide: MatSnackBar, useClass: MatSnackBarStub }
      ],
    })
      .compileComponents();
    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    sessionService.sessionInformation = sessionInfos;
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should delete session', () => {
    const deleteSpy = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of(void 0));
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
    const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation(() => new Promise<boolean>((resolve, _) => resolve(true)));
    component.delete();
    expect(deleteSpy).toHaveBeenCalledTimes(1);
    expect(snackBarSpy).toHaveBeenCalledTimes(1);
    expect(navigateSpy).toHaveBeenCalledTimes(1);
  });
});

