import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';

import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;
    let authService: AuthService;
    let router: Router;
    let controller: HttpTestingController;
    const pathService: string = 'api/auth';

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [RegisterComponent],
            imports: [
                RouterTestingModule,
                BrowserAnimationsModule,
                HttpClientTestingModule,
                ReactiveFormsModule,
                MatCardModule,
                MatFormFieldModule,
                MatIconModule,
                MatInputModule
            ],
            providers: [AuthService]
        })
            .compileComponents();

        fixture = TestBed.createComponent(RegisterComponent);
        component = fixture.componentInstance;
        authService = TestBed.inject(AuthService);
        router = TestBed.inject(Router);
        controller = TestBed.inject(HttpTestingController);
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should register', () => {
        const registerSpy = jest.spyOn(authService, 'register');
        const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation(() => new Promise<boolean>((resolve, _) => resolve(true)));
        component.submit();
        fixture.whenStable().then(() => {
            const request = controller.expectOne(pathService + '/register');
            request.flush('Registered successfuly');
            controller.verify();
            expect(registerSpy).toHaveBeenCalledTimes(1);
            expect(navigateSpy).toHaveBeenCalledTimes(1);
            expect(component.onError).toBeFalsy();
        });
    });

    it('should set on error to true when the register request fails', () => {
        const registerSpy = jest.spyOn(authService, 'register');
        component.submit();
        fixture.whenStable().then(() => {
            const request = controller.expectOne(pathService + '/register');
            request.flush('Bad request', { status: 400, statusText: 'BAD_REQUEST' });
            controller.verify();
            expect(registerSpy).toHaveBeenCalledTimes(1);
            expect(component.onError).toBeTruthy();
        });
    });
});
