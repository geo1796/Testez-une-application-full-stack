import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from '../../services/auth.service';

import { LoginComponent } from './login.component';
import { expect } from '@jest/globals';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let authService: AuthService;
    let sessionService: SessionService;
    let router: Router;
    let controller: HttpTestingController;
    const pathService: string = 'api/auth';

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [LoginComponent],
            providers: [AuthService, SessionService],
            imports: [
                RouterTestingModule,
                BrowserAnimationsModule,
                HttpClientTestingModule,
                MatCardModule,
                MatIconModule,
                MatFormFieldModule,
                MatInputModule,
                ReactiveFormsModule]
        })
            .compileComponents();
        fixture = TestBed.createComponent(LoginComponent);
        component = fixture.componentInstance;
        authService = TestBed.inject(AuthService);
        sessionService = TestBed.inject(SessionService);
        router = TestBed.inject(Router);
        controller = TestBed.inject(HttpTestingController);
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should log in successfuly', () => {
        const expectedSessionInfo: SessionInformation =
            { id: 1, username: 'openclassrooms', firstName: 'hello', lastName: 'world', token: 'jwt', type: 'Bearer', admin: false };
        const authSpy = jest.spyOn(authService, 'login');
        const sessionSpy = jest.spyOn(sessionService, 'logIn');
        const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation(() => new Promise<boolean>((resolve, _) => resolve(true)));
        component.submit();
        fixture.whenStable().then(() => {
            const request = controller.expectOne(pathService + '/login');
            request.flush(expectedSessionInfo);
            controller.verify();
            expect(authSpy).toHaveBeenCalledTimes(1);
            expect(sessionSpy).toHaveBeenCalledTimes(1);
            expect(navigateSpy).toHaveBeenCalledTimes(1);
            expect(component.onError).toBeFalsy();
            expect(sessionService.isLogged).toBeTruthy();
            expect(sessionService.sessionInformation!).toBe(expectedSessionInfo);
        });
    });

    it('should set on error to true when the login request fails', () => {
        const authSpy = jest.spyOn(authService, 'login');
        component.submit();
        fixture.whenStable().then(() => {
            const request = controller.expectOne(pathService + '/login');
            request.flush('Unauthorized', { status: 401, statusText: 'UNAUTHORIZED' });
            controller.verify();
            expect(authSpy).toHaveBeenCalledTimes(1);
            expect(component.onError).toBeTruthy();
            expect(sessionService.isLogged).toBeFalsy();
        });
    });
});
