import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
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
import { Router } from '@angular/router';
import { MockRouter, MockSnackBar } from 'src/app/spec-utils/mocks';
import { User } from 'src/app/interfaces/user.interface';

describe('MeComponent', () => {
    let component: MeComponent;
    let fixture: ComponentFixture<MeComponent>;
    let sessionService: SessionService;
    let userService: UserService;
    let router: Router;
    let matSnackBar: MatSnackBar;
    let controller: HttpTestingController;
    const pathService: string = 'api/user';

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [MeComponent],
            imports: [
                RouterTestingModule,
                MatSnackBarModule,
                HttpClientTestingModule,
                MatCardModule,
                MatFormFieldModule,
                MatIconModule,
                MatInputModule
            ],
            providers: [
                SessionService,
                UserService,
                { provide: MatSnackBar, useClass: MockSnackBar },
                { provide: Router, useClass: MockRouter }
            ],
        })
            .compileComponents();

        fixture = TestBed.createComponent(MeComponent);
        component = fixture.componentInstance;
        sessionService = TestBed.inject(SessionService);
        sessionService.logIn({ token: '', type: '', id: 1, username: '', firstName: '', lastName: '', admin: false });
        userService = TestBed.inject(UserService);
        router = TestBed.inject(Router);
        matSnackBar = TestBed.inject(MatSnackBar);
        controller = TestBed.inject(HttpTestingController);
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should delete', () => {
        expect(sessionService.isLogged).toBeTruthy();
        const deleteSpy = jest.spyOn(userService, 'delete');
        const logoutSpy = jest.spyOn(sessionService, 'logOut');
        component.delete();
        fixture.whenStable().then(() => {
            const requests = controller.match(pathService + '/1');
            expect(requests.length).toBe(2);
            const user: User = { firstName: '', lastName: '', id: 1, admin: false, password: '', email: '', createdAt: new Date() };
            requests[0].flush(user);
            requests[1].flush('user deleted');
            expect(deleteSpy).toHaveBeenCalledTimes(1);
            expect(logoutSpy).toHaveBeenCalledTimes(1);
            expect(component.user).toBe(user);
            expect(sessionService.isLogged).toBeFalsy();
        });
    });
});
