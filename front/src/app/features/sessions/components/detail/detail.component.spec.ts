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
        MatCardModule,
        NoopAnimationsModule     
      ],
    providers: [
      FormBuilder,                    // <-- Ajouté ici
      { provide: SessionService, useValue: mockSessionService },
      { provide: SessionApiService, useValue: mockSessionApiService },
      { provide: TeacherService, useValue: mockTeacherService },
      { provide: Router, useValue: mockRouter },
      { provide: ActivatedRoute, useValue: mockActivatedRoute }
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

describe('DetailComponent avec services mockés', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  // Mocks des services avec des méthodes jest.fn()
  const mockSessionService = {
    sessionInformation: { id: 1, admin: true }
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
        // Modules Angular nécessaires
        BrowserAnimationsModule,
        MatSnackBarModule,
        RouterTestingModule,
        ReactiveFormsModule,
        MatIconModule,
        MatCardModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    // Définir les retours mockés par défaut pour les méthodes
    mockSessionApiService.detail.mockReturnValue(of({
      id: 123,
      name: 'Session Test',
      description: 'Description de test',
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

  it('doit créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('doit récupérer la session et le professeur au démarrage', fakeAsync(() => {
    component.ngOnInit();
    tick();
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('123');
    expect(component.session).toBeDefined();
    expect(component.isParticipate).toBe(true);
    expect(mockTeacherService.detail).toHaveBeenCalledWith('1');
    expect(component.teacher).toBeDefined();
  }));

  it('doit appeler window.history.back()', () => {
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toHaveBeenCalled();
  });

  it('doit supprimer la session et naviguer', fakeAsync(() => {
    const snackBarSpy = jest.spyOn(component['matSnackBar'], 'open');

    component.delete();
    tick();
    flush();

    expect(mockSessionApiService.delete).toHaveBeenCalledWith('123');
    expect(snackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  }));

  it('doit participer à la session et rafraîchir', fakeAsync(() => {
    component.participate();
    tick();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('123', '1');
    expect(mockSessionApiService.detail).toHaveBeenCalled();
  }));

  it('doit se désinscrire de la session et rafraîchir', fakeAsync(() => {
    component.unParticipate();
    tick();
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('123', '1');
    expect(mockSessionApiService.detail).toHaveBeenCalled();
  }));

  it('doit définir isParticipate à false si l’utilisateur n’est pas dans la session', fakeAsync(() => {
    mockSessionApiService.detail.mockReturnValueOnce(of({
      id: 123,
      name: 'Session Test',
      description: 'Description',
      date: new Date(),
      teacher_id: 1,
      users: [2, 3] // L’ID 1 n’est pas dans la liste
    }));

    component.ngOnInit();
    tick();

    expect(component.isParticipate).toBe(false);
  }));

});