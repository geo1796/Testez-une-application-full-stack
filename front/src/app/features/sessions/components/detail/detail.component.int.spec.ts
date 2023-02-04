import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';

import { DetailComponent } from './detail.component';
import { TeacherService } from 'src/app/services/teacher.service';

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
  let teacherService: TeacherService;
  let matSnackBar: MatSnackBar;
  let router: Router;
  let controller: HttpTestingController;
  const pathSessionService: string = 'api/session';
  const pathTeacherService: string = 'api/teacher';
  const sessionInfos: SessionInformation =
    { username: "", firstName: "", lastName: "", id: 1, admin: false, token: "", type: "" };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        RouterTestingModule
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
    sessionService.sessionInformation = sessionInfos;
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    matSnackBar = TestBed.inject(MatSnackBar);
    controller = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    component.sessionId = '1';
    component.userId = '1';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should delete session', () => {
    controller.expectOne(pathSessionService + "/1");
    const deleteSpy = jest.spyOn(sessionApiService, 'delete');
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
    const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation(() => new Promise<boolean>((resolve, _) => resolve(true)));
    component.delete();
    fixture.whenStable().then(() => {
      const request = controller.expectOne(pathSessionService + '/1');
      request.flush('session deleted');
      controller.verify();
      expect(deleteSpy).toHaveBeenCalledTimes(1);
      expect(snackBarSpy).toHaveBeenCalledTimes(1);
      expect(navigateSpy).toHaveBeenCalledTimes(1);
    });
  });

  it('should participate to session', () => {
    const participateSpy = jest.spyOn(sessionApiService, 'participate');
    const fetchSpy = jest.spyOn(sessionApiService, 'detail');
    const teacherDetailSpy = jest.spyOn(teacherService, 'detail');
    component.participate();
    fixture.whenStable().then(() => {
      const participateRequest = controller.expectOne(pathSessionService + '/1/participate/1');
      participateRequest.flush('participate to session');
      const fetchRequests = controller.match(pathSessionService + "/1");
      expect(fetchRequests.length).toBe(2);
      const session: Session = { name: '', description: '', date: new Date(), teacher_id: 1, users: [1] }
      fetchRequests[0].flush(session);
      fetchRequests[1].flush(session);
      const teacherRequests = controller.match(pathTeacherService + "/1");
      expect(teacherRequests.length).toBe(2);
      const teacher: Teacher = { id: 1, firstName: 'Hello', lastName: 'World', createdAt: new Date(), updatedAt: new Date() };
      teacherRequests[0].flush(teacher);
      teacherRequests[1].flush(teacher);
      controller.verify();
      expect(participateSpy).toHaveBeenCalledTimes(1);
      expect(fetchSpy).toHaveBeenCalledTimes(1);
      expect(teacherDetailSpy).toHaveBeenCalledTimes(2);
      expect(component.isParticipate).toBeTruthy();
    });
  });

  it('should unparticipate to session', () => {
    const unParticipateSpy = jest.spyOn(sessionApiService, 'unParticipate');
    const fetchSpy = jest.spyOn(sessionApiService, 'detail');
    const teacherDetailSpy = jest.spyOn(teacherService, 'detail');
    component.unParticipate();
    fixture.whenStable().then(() => {
      const participateRequest = controller.expectOne(pathSessionService + '/1/participate/1');
      participateRequest.flush('unparticipate to session');
      const fetchRequests = controller.match(pathSessionService + "/1");
      expect(fetchRequests.length).toBe(2);
      const session: Session = { name: '', description: '', date: new Date(), teacher_id: 1, users: [] }
      fetchRequests[0].flush(session);
      fetchRequests[1].flush(session);
      const teacherRequests = controller.match(pathTeacherService + "/1");
      expect(teacherRequests.length).toBe(2);
      const teacher: Teacher = { id: 1, firstName: 'Hello', lastName: 'World', createdAt: new Date(), updatedAt: new Date() };
      teacherRequests[0].flush(teacher);
      teacherRequests[1].flush(teacher);
      controller.verify();
      expect(unParticipateSpy).toHaveBeenCalledTimes(1);
      expect(fetchSpy).toHaveBeenCalledTimes(1);
      expect(teacherDetailSpy).toHaveBeenCalledTimes(2);
      expect(component.isParticipate).toBeFalsy();
    });
  });
});

