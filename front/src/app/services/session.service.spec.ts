import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in & log out', () => {
    expect(service.isLogged).toBeFalsy();
    service.logIn({firstName: 'hello', lastName: 'world', admin: false, token: 'abc', type: 'Bearer', id: 1, username: 'openclassrooms'})
    expect(service.isLogged).toBeTruthy();
    service.logOut();
    expect(service.isLogged).toBeFalsy();
  }) 
});
