import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { of, BehaviorSubject } from 'rxjs';
import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { NgZone } from '@angular/core';
import { expect } from '@jest/globals';

describe('AppComponent', () => {
  let isLoggedSubject: BehaviorSubject<boolean>;
  let sessionServiceSpy: any;
  let component: AppComponent;
  let ngZone: NgZone;

  beforeEach(async () => {
    isLoggedSubject = new BehaviorSubject<boolean>(false);

    sessionServiceSpy = {
      $isLogged: jest.fn(() => isLoggedSubject.asObservable()),
      logIn: jest.fn((user: any) => {
        sessionServiceSpy.sessionInformation = user;
        isLoggedSubject.next(true);
      }),
      logOut: jest.fn(() => {
        sessionServiceSpy.sessionInformation = undefined;
        isLoggedSubject.next(false);
      }),
      sessionInformation: undefined,
    };

    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientModule, MatToolbarModule],
      declarations: [AppComponent],
      providers: [{ provide: SessionService, useValue: sessionServiceSpy }],
    }).compileComponents();
    
    ngZone = TestBed.inject(NgZone);
    const fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should return observable from $isLogged', (done) => {
  
    isLoggedSubject.next(true);

    component.$isLogged().subscribe((logged) => {
      expect(logged).toBe(true);
      done();
    });

    expect(sessionServiceSpy.$isLogged).toHaveBeenCalled();
  });

 it('should call logOut and navigate on logout', () => {
    const routerSpy = jest.spyOn((component as any).router, 'navigate');
    ngZone.run(() => {
      component.logout();
    });

    expect(sessionServiceSpy.logOut).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['']);
  });



});