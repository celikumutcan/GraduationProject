import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EvaluateInternStudentComponent } from './evaluate-intern-student.component';

describe('EvaluateInternStudentComponent', () => {
  let component: EvaluateInternStudentComponent;
  let fixture: ComponentFixture<EvaluateInternStudentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EvaluateInternStudentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EvaluateInternStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
