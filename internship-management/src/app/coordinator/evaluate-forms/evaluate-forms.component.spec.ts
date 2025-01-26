import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EvaluateFormsComponent } from './evaluate-forms.component';

describe('EvaluateFormsComponent', () => {
  let component: EvaluateFormsComponent;
  let fixture: ComponentFixture<EvaluateFormsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EvaluateFormsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EvaluateFormsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
