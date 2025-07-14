import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { DetailComponent } from './detail.component';
import { TeacherService } from '../../../../services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of,throwError } from 'rxjs';
import { fakeAsync, tick, flush  } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';  
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';



describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  // Mocks
  const mockSessionService = {
    sessionInformation: {
      id: 1,
      admin: true
    }
  };

  const mockSessionApiService = {
    detail: jest.fn(),
    delete: jest.fn(),
    participate: jest.fn(),
    unParticipate: jest.fn()
  };

  const mockTeacherService = {
    detail: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn(() => '123')
      }
    }
  };

beforeEach(async () => {
  await TestBed.configureTestingModule({
    declarations: [DetailComponent],
     imports: [
        BrowserAnimationsModule,  // <-- Ajout obligatoire pour les animations
        MatSnackBarModule,
        RouterTestingModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatIconModule,
        MatCardModule     
      ],
    providers: [
      FormBuilder,                    // <-- Ajouté ici
      { provide: SessionService, useValue: mockSessionService },
      { provide: SessionApiService, useValue: mockSessionApiService },
      { provide: TeacherService, useValue: mockTeacherService },
      { provide: Router, useValue: mockRouter },
      { provide: ActivatedRoute, useValue: mockActivatedRoute },
      { provide: SessionService, useValue: mockSessionService }
    ]
  }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    // Setup default mock return values
    mockSessionApiService.detail.mockReturnValue(of({
      id: 123,
      name: 'Session Test',
      description: 'Desc',
      date: new Date(),
      teacher_id: 1,
      users: [1]
    }));

    mockTeacherService.detail.mockReturnValue(of({
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      createdAt: new Date(),
      updatedAt: new Date()
    }));

    mockSessionApiService.delete.mockReturnValue(of({}));
    mockSessionApiService.participate.mockReturnValue(of(void 0));
    mockSessionApiService.unParticipate.mockReturnValue(of(void 0));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session and teacher on init', fakeAsync(() => {
    component.ngOnInit();
    tick();
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('123');
    expect(component.session).toBeDefined();
    expect(component.isParticipate).toBe(true);
    expect(mockTeacherService.detail).toHaveBeenCalledWith('1');
    expect(component.teacher).toBeDefined();
  }));

  it('should call back to history.back', () => {
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toHaveBeenCalled();
  });

it('should delete session and navigate', fakeAsync(() => {
  const snackSpy = jest.spyOn(component['matSnackBar'], 'open');
  
  component.delete();
  
  tick();   // fait avancer les timers liés à l'observable
  flush();  // nettoie tous timers restants, évite les erreurs "timer still in queue"

  expect(mockSessionApiService.delete).toHaveBeenCalledWith('123');
  expect(snackSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
  expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
}));

  it('should participate and fetch session', fakeAsync(() => {
    component.participate();
    tick();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('123', '1');
    expect(mockSessionApiService.detail).toHaveBeenCalled();
  }));

  it('should unParticipate and fetch session', fakeAsync(() => {
    component.unParticipate();
    tick();
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('123', '1');
    expect(mockSessionApiService.detail).toHaveBeenCalled();
  }));

/*
it('should not crash if delete fails', fakeAsync(() => {
  jest.spyOn(console, 'error').mockImplementation(() => {}); // ignore log d'erreur
  mockSessionApiService.delete.mockReturnValueOnce(
    throwError(new Error('Delete failed'))  // ✅ version compatible
  );
  component.delete();
  tick();
  expect(true).toBe(true);
}));
*/
it('should set isParticipate to false if user not in session users', fakeAsync(() => {
  mockSessionApiService.detail.mockReturnValueOnce(of({
    id: 123,
    name: 'Session Test',
    description: 'Desc',
    date: new Date(),
    teacher_id: 1,
    users: [2,3]  // pas l’id 1 du mockSessionService
  }));

  component.ngOnInit();
  tick();

  expect(component.isParticipate).toBe(false);
}));

});
