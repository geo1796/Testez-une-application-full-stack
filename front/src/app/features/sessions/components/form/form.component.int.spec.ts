import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { SessionService } from 'src/app/services/session.service';
import { MockSnackBar, MockRouter } from 'src/app/spec-utils/mocks';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';

describe('FormComponent', () => {
    let component: FormComponent;
    let fixture: ComponentFixture<FormComponent>;
    let sessionService: SessionService;
    let sessionApiService: SessionApiService;
    let router: Router;
    let matSnackBar: MatSnackBar;
    let controller: HttpTestingController;
    const sessionInfos: SessionInformation = { username: "", firstName: "", lastName: "", id: 0, admin: false, token: "", type: "" };
    const pathSessionService = 'api/session';
    const pathTeacherService = 'api/teacher';

    beforeEach(async () => {
        await TestBed.configureTestingModule({

            imports: [
                RouterTestingModule,
                HttpClientTestingModule,
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
                { provide: Router, useClass: MockRouter },
                { provide: MatSnackBar, useClass: MockSnackBar },
                { provide: ActivatedRoute, useValue: { snapshot: { paramMap: convertToParamMap({ 'id': '1' }) } } }
            ],
            declarations: [FormComponent]
        })
            .compileComponents();

        fixture = TestBed.createComponent(FormComponent);
        component = fixture.componentInstance;
        sessionService = TestBed.inject(SessionService);
        sessionService.sessionInformation = sessionInfos;
        sessionApiService = TestBed.inject(SessionApiService);
        controller = TestBed.inject(HttpTestingController);
        router = TestBed.inject(Router);
        matSnackBar = TestBed.inject(MatSnackBar);
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should create session', () => {
        const createSpy = jest.spyOn(sessionApiService, 'create');
        const snackBarSpy = jest.spyOn(matSnackBar, 'open');
        component.onUpdate = false;
        component.submit();
        fixture.whenStable().then(() => {
            const teacherRequest = controller.expectOne(pathTeacherService);
            teacherRequest.flush([]);
            const createRequest = controller.expectOne(pathSessionService);
            const session: Session = { name: '', description: '', date: new Date(), teacher_id: 1, users: [1] }
            createRequest.flush(session);
            controller.verify();
            expect(createSpy).toHaveBeenCalledTimes(1);
            expect(snackBarSpy).toHaveBeenCalledTimes(1);
        });
    });

    it('should update session', () => {
        const updateSpy = jest.spyOn(sessionApiService, 'update');
        const snackBarSpy = jest.spyOn(matSnackBar, 'open');
        component.onUpdate = true;
        // @ts-ignore: force this private property value for testing.
        component.id = '1';
        component.submit();
        fixture.whenStable().then(() => {
            const teacherRequest = controller.expectOne(pathTeacherService);
            teacherRequest.flush([]);
            const updateRequest = controller.expectOne(pathSessionService + '/1');
            const session: Session = { name: '', description: '', date: new Date(), teacher_id: 1, users: [1] }
            updateRequest.flush(session);
            controller.verify();
            expect(updateSpy).toHaveBeenCalledTimes(1);
            expect(snackBarSpy).toHaveBeenCalledTimes(1);
        });
    });

    it('should init properly when the user is admin', () => {
        sessionService.sessionInformation!.admin = true;
        jest.spyOn(router, 'url', 'get').mockImplementation(() => 'update');
        component.ngOnInit();
        fixture.whenStable().then(() => {
            const teacherRequest = controller.expectOne(pathTeacherService);
            teacherRequest.flush([]);
            const detailRequest = controller.expectOne(pathSessionService + '/1');
            const session: Session = { name: '', description: '', date: new Date(), teacher_id: 1, users: [1] }
            detailRequest.flush(session);
            controller.verify();
            expect(component.onUpdate).toBeTruthy();
        });
    });

});
