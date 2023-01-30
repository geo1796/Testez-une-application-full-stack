import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Session } from '../interfaces/session.interface';

import { SessionApiService } from './session-api.service';

describe('SessionsService', () => {
  let service: SessionApiService;
  let controller: HttpTestingController;
  let apiUrl: string = 'api/session';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    controller = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all sessions', () => {
    const expectedSessions: Session[] = [{ name: 'hello', description: 'world', date: new Date(), teacher_id: 1, users: [1] }];
    var actualSessions: Session[] = [];
    service.all().subscribe({
      next: sessions => actualSessions = sessions
    });
    const request = controller.expectOne(apiUrl);
    request.flush(expectedSessions);
    expect(actualSessions).toEqual(expectedSessions);
    controller.verify();
  });

  it('should get session by id', () => {
    const expectedSession: Session = { name: 'hello', description: 'world', date: new Date(), teacher_id: 1, users: [1] };
    var actualSession: Session | undefined;
    service.detail('id').subscribe({
      next: session => actualSession = session
    });
    const request = controller.expectOne(apiUrl + '/id');
    request.flush(expectedSession);
    expect(actualSession).toEqual(expectedSession);
    controller.verify();
  });

  it('should delete session', () => {
    service.delete('id').subscribe({
      next: _ => {}
    });
    const request = controller.expectOne(apiUrl + '/id');
    controller.verify();
  });

  it('should create session', () => {
    const expectedSession: Session = { name: 'hello', description: 'world', date: new Date(), teacher_id: 1, users: [1] };
    var actualSession: Session | undefined;
    service.create(expectedSession).subscribe({
      next: session => actualSession = session
    });
    const request = controller.expectOne(apiUrl);
    request.flush(expectedSession);
    expect(actualSession).toEqual(expectedSession);
    controller.verify();
  });

  it('should update session', () => {
    const expectedSession: Session = { name: 'hello', description: 'world', date: new Date(), teacher_id: 1, users: [1] };
    var actualSession: Session | undefined;
    service.update('id', expectedSession).subscribe({
      next: session => actualSession = session
    });
    const request = controller.expectOne(apiUrl + '/id');
    request.flush(expectedSession);
    expect(actualSession).toEqual(expectedSession);
    controller.verify();
  });

  it('should participate', () => {
    service.participate('sessionId', 'userId').subscribe({
      next: _ => {}
    });
    const request = controller.expectOne(apiUrl + '/sessionId/participate/userId');
    controller.verify();
  });

  it('should unParticipate', () => {
    service.unParticipate('sessionId', 'userId').subscribe({
      next: _ => {}
    });
    const request = controller.expectOne(apiUrl + '/sessionId/participate/userId');
    controller.verify();
  });

});
