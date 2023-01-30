import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Session } from '../interfaces/session.interface';

import { SessionApiService } from './session-api.service';

describe('SessionsService', () => {
  let service: SessionApiService;
  let controller: HttpTestingController;

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

  it('get all sessions', () => {
    const expectedSessions: Session[] = [{ name: 'hello', description: 'world', date: new Date(), teacher_id: 1, users: [1] }];
    var actualSessions: Session[] = [];
    service.all().subscribe({
      next: sessions => actualSessions = sessions
    });
    const request = controller.expectOne('api/session');
    request.flush(expectedSessions);
    expect(actualSessions).toEqual(expectedSessions);
  });

});
