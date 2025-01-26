import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentAffairsComponent } from './student-affairs.component';

describe('StudentAffairsComponent', () => {
  let component: StudentAffairsComponent;
  let fixture: ComponentFixture<StudentAffairsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StudentAffairsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentAffairsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
