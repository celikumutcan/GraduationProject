import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckFormsComponent } from './check-forms.component';

describe('CheckFormsComponent', () => {
  let component: CheckFormsComponent;
  let fixture: ComponentFixture<CheckFormsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CheckFormsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CheckFormsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
