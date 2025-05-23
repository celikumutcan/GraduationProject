import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InternshipFormsComponent } from './internship-forms.component';

describe('InternshipFormsComponent', () => {
  let component: InternshipFormsComponent;
  let fixture: ComponentFixture<InternshipFormsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InternshipFormsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InternshipFormsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
