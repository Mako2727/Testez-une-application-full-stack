import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule,FormBuilder } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule,MatSnackBar } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { fakeAsync, tick } from '@angular/core/testing';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockRouter = {
    url: '/sessions/create',
    navigate: jest.fn()
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('123')
      }
    }
  };

  const mockSessionApiService = {
    detail: jest.fn(),
    create: jest.fn(),
    update: jest.fn()
  };

  const mockTeacherService = {
    all: jest.fn()
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

beforeEach(async () => {
  await TestBed.configureTestingModule({
    declarations: [FormComponent],
    imports: [
      ReactiveFormsModule,
      MatSnackBarModule,
      HttpClientModule,
      RouterTestingModule,
      BrowserAnimationsModule,
      MatFormFieldModule,
      MatInputModule,
      MatSelectModule,
      MatIconModule,
      MatCardModule
    ],
    providers: [
      FormBuilder,
      { provide: SessionService, useValue: mockSessionService },
      { provide: Router, useValue: mockRouter },
      { provide: ActivatedRoute, useValue: mockActivatedRoute },
      { provide: SessionApiService, useValue: mockSessionApiService },
      { provide: TeacherService, useValue: mockTeacherService },
      { provide: MatSnackBar, useValue: mockMatSnackBar }
    ]
  }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    // Par défaut mock teacherService.all() retourne un Observable vide
    mockTeacherService.all.mockReturnValue(of([]));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect non-admin user on ngOnInit', () => {
    mockSessionService.sessionInformation.admin = false;
    component.ngOnInit();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    mockSessionService.sessionInformation.admin = true; // reset pour autres tests
  });

  it('should call detail and initForm on ngOnInit when url contains "update"', fakeAsync(() => {
    mockRouter.url = '/sessions/update/123';
    const sessionMock = {
      name: 'Session Test',
      date: '2023-07-14T00:00:00.000Z',
      teacher_id: 42,
      description: 'Desc test'
    };
    mockSessionApiService.detail.mockReturnValue(of(sessionMock));

    const initFormSpy = jest.spyOn(component as any, 'initForm');

    component.ngOnInit();
    tick();

    expect(mockSessionApiService.detail).toHaveBeenCalledWith('123');
    expect(initFormSpy).toHaveBeenCalledWith(sessionMock);
    expect(component.onUpdate).toBe(true);
  }));

  it('should call initForm with no params on ngOnInit when url does not contain "update"', () => {
    mockRouter.url = '/sessions/create';
    const initFormSpy = jest.spyOn(component as any, 'initForm');
    component.ngOnInit();
    expect(initFormSpy).toHaveBeenCalledWith();
    expect(component.onUpdate).toBe(false);
  });

  it('should call create on submit when not updating', () => {
    component.onUpdate = false;
    component.sessionForm = component['fb'].group({
      name: ['Session 1'],
      date: ['2023-07-14'],
      teacher_id: [42],
      description: ['Description']
    });
    mockSessionApiService.create.mockReturnValue(of({}));

    const exitSpy = jest.spyOn(component as any, 'exitPage');

    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalledWith(component.sessionForm?.value);
    expect(exitSpy).toHaveBeenCalledWith('Session created !');
  });

  it('should call update on submit when updating', () => {
    component.onUpdate = true;
    component['id'] = '123';
    component.sessionForm = component['fb'].group({
      name: ['Session 1'],
      date: ['2023-07-14'],
      teacher_id: [42],
      description: ['Description']
    });
    mockSessionApiService.update.mockReturnValue(of({}));

    const exitSpy = jest.spyOn(component as any, 'exitPage');

    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith('123', component.sessionForm?.value);
    expect(exitSpy).toHaveBeenCalledWith('Session updated !');
  });

  it('exitPage should show snackbar and navigate', () => {
    component['exitPage']('Test message');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Test message', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });
});