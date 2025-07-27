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

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  // Mocks
  const mockUser: User = {
    id: 1,
    email: 'john@example.com',
    firstName: 'John',
    lastName: 'Doe',
    admin: false,
    password: 'secure',
    createdAt: new Date('2023-01-01'),
    updatedAt: new Date('2023-01-02')
  };

  const mockSessionService = {
    sessionInformation: {
      id: 1,
      admin: true
    },
    logOut: jest.fn()
  };

  const mockUserService = {
    getById: jest.fn(),
    delete: jest.fn()
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
    HttpClientModule,
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
    mockUserService.getById.mockReturnValue(of(mockUser));
    component.ngOnInit();
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUser);
  });

  it('should call window.history.back on back()', () => {
    const backSpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    component.back();
    expect(backSpy).toHaveBeenCalled();
  });

  it('should delete user, show snackbar, log out and navigate', () => {
    mockUserService.delete.mockReturnValue(of({}));
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

describe('MeComponent - Integration Test', () => {
  let fixture: ComponentFixture<MeComponent>;
  let component: MeComponent;
  let httpMock: HttpTestingController; // <--- bien déclaré ici
  let router: Router;
  let snackBar: MatSnackBar;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        HttpClientTestingModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatSnackBarModule
      ],
      providers: [
        SessionService,
        UserService,
        MatSnackBar,
        Router
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController); // <--- injection ici
    router = TestBed.inject(Router);
    snackBar = TestBed.inject(MatSnackBar);

    jest.spyOn(router, 'navigate');
    jest.spyOn(snackBar, 'open');
    fixture.detectChanges();
  });

  // tes tests ...

  afterEach(() => {
    httpMock.verify(); // <--- ici il doit être défini
  });
});