import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EvaluateAssignedReportsComponent } from './evaluate-assigned-reports.component';

describe('EvaluateAssignedReportsComponent', () => {
  let component: EvaluateAssignedReportsComponent;
  let fixture: ComponentFixture<EvaluateAssignedReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EvaluateAssignedReportsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EvaluateAssignedReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
