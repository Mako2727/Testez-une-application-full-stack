import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

import { RegisterComponent } from './register.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  const mockAuthService = {
    register: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

beforeEach(async () => {
  await TestBed.configureTestingModule({
    declarations: [RegisterComponent],
    imports: [
      ReactiveFormsModule,
      HttpClientModule,
      MatCardModule,
      MatFormFieldModule,
      MatInputModule,
      MatIconModule,
      BrowserAnimationsModule
    ],
    providers: [
      FormBuilder,
      { provide: AuthService, useValue: mockAuthService },
      { provide: Router, useValue: mockRouter }
    ]
  }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
     fixture.detectChanges();
  });

  it('should create the register component', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to /login on successful submit', () => {
    mockAuthService.register.mockReturnValue(of(void 0)); // simulate void Observable

    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '123456'
    });

    component.submit();

    
    expect(mockAuthService.register).toHaveBeenCalledWith({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '123456'
    });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBe(false);
  });

  it('should set onError to true on register error', () => {
    mockAuthService.register.mockReturnValue(throwError(() => new Error('Register failed')));
//entest d integration je mock en http client la reponse predefini!!
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '123456'
    });

    component.submit();

    expect(component.onError).toBe(true);
  });
});

//
// TESTS D’INTÉGRATION
//
describe('RegisterComponent - Integration Test', () => {
  let fixture: ComponentFixture<RegisterComponent>;
  let component: RegisterComponent;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule, // <-- important !
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        BrowserAnimationsModule
      ],
      providers: [FormBuilder]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    httpMock = TestBed.inject(HttpTestingController);

    jest.spyOn(router, 'navigate'); // espionner la navigation
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'aucune requête HTTP n'est restée ouverte
  });

  it('should send HTTP POST and navigate on success', fakeAsync(() => {
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '123456'
    });

    component.submit();

   const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');

    const mockResponse = { message: 'User registered successfully' };
    req.flush(mockResponse);

    tick();
    fixture.detectChanges();

    expect(component.onError).toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  }));

  it('should set onError to true on HTTP error', fakeAsync(() => {
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '123456'
    });

    component.submit();

    const req = httpMock.expectOne('http://localhost:9090/api/auth/register');
    expect(req.request.method).toBe('POST');

    req.flush(
      { message: 'Erreur' },
      { status: 400, statusText: 'Bad Request' }
    );

    tick();
    fixture.detectChanges();

    expect(component.onError).toBe(true);
    expect(router.navigate).not.toHaveBeenCalled();
  }));
});