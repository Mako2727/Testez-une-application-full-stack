import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule,MatSnackBar } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { fakeAsync, tick } from '@angular/core/testing';

import { MeComponent } from './me.component';
import { User } from '../../interfaces/user.interface';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';




const mockUser = {
  id: 1,
  email: 'john@example.com',
  firstName: 'John',
  lastName: 'Doe',
  admin: false,
  password: 'secure',
  createdAt: new Date('2023-01-01'),
  updatedAt: new Date('2023-01-02')
};

describe('MeComponent - Unit Tests', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  // Mocks de services avec retour Observable (of)
  const mockSessionService = {
    sessionInformation: { id: 1, admin: true },
    logOut: jest.fn()
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(mockUser)),
    delete: jest.fn().mockReturnValue(of({}))
  };

  const mockSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatSnackBarModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

it('should load user on ngOnInit', () => {
  // Mock complet de sessionService avec sessionInformation défini
  mockSessionService.sessionInformation = { id: 1, admin: true };

  // Mock du userService pour retourner un observable du mockUser
  mockUserService.getById.mockReturnValue(of(mockUser));

  // Injecter le mock dans le composant (si besoin)
  (component as any).sessionService = mockSessionService;
  (component as any).userService = mockUserService;

  // Appeler ngOnInit, qui doit maintenant fonctionner sans erreur
  component.ngOnInit();

  // Vérifier que getById a été appelé avec l'id '1' (toString() donne '1')
  expect(mockUserService.getById).toHaveBeenCalledWith('1');

  // Vérifier que la propriété user est bien mise à jour
  expect(component.user).toEqual(mockUser);
});

  it('should call window.history.back on back()', () => {
    const backSpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    component.back();
    expect(backSpy).toHaveBeenCalled();
  });

  it('should delete user, show snackbar, log out and navigate', () => {
    component.delete();

    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSnackBar.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  // Mock complet de sessionService avec sessionInformation bien initialisé
  const mockSessionService = {
    sessionInformation: { id: 1, admin: true },
    logOut: jest.fn()
  };

  // Mock complet de userService avec méthodes getById et delete qui retournent des observables
  const mockUserService = {
    getById: jest.fn().mockReturnValue(of({
      id: 1,
      email: 'john@example.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: false,
      password: 'secure',
      createdAt: new Date('2023-01-01'),
      updatedAt: new Date('2023-01-02')
    })),
    delete: jest.fn().mockReturnValue(of({}))
  };

  // Mock snackBar pour tester les appels open()
  const mockSnackBar = {
    open: jest.fn()
  };

  // Mock router pour tester la navigation
  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        NoopAnimationsModule,  // <-- Ajout important pour éviter l'erreur d'animation
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatSnackBarModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load user on ngOnInit', () => {
    component.ngOnInit();
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toBeTruthy();
    expect(component.user?.firstName).toBe('John');
  });

  it('should call window.history.back on back()', () => {
    const backSpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    component.back();
    expect(backSpy).toHaveBeenCalled();
  });

  it('should delete user, show snackbar, log out and navigate', () => {
    component.delete();

    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});