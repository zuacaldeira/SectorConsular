import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProgressBarComponent } from './progress-bar.component';

describe('ProgressBarComponent', () => {
  let component: ProgressBarComponent;
  let fixture: ComponentFixture<ProgressBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgressBarComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ProgressBarComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have default value of 0', () => {
    expect(component.value).toBe(0);
  });

  it('should have default color of var(--color-blue)', () => {
    expect(component.color).toBe('var(--color-blue)');
  });

  it('should have default showLabel of true', () => {
    expect(component.showLabel).toBe(true);
  });

  it('should accept custom value input', () => {
    component.value = 75;
    fixture.detectChanges();
    expect(component.value).toBe(75);
  });

  it('should accept custom color input', () => {
    component.color = 'var(--angola-red)';
    fixture.detectChanges();
    expect(component.color).toBe('var(--angola-red)');
  });

  it('should accept custom showLabel input', () => {
    component.showLabel = false;
    fixture.detectChanges();
    expect(component.showLabel).toBe(false);
  });

  it('should render the progress fill with the correct width', () => {
    component.value = 50;
    fixture.detectChanges();
    const fill = fixture.nativeElement.querySelector('.progress-fill');
    expect(fill).toBeTruthy();
    expect(fill.style.width).toBe('50%');
  });

  it('should render the progress fill with the correct background color', () => {
    component.value = 30;
    component.color = 'red';
    fixture.detectChanges();
    const fill = fixture.nativeElement.querySelector('.progress-fill');
    expect(fill.style.background).toBe('red');
  });

  it('should show the label when showLabel is true', () => {
    component.value = 42.5;
    component.showLabel = true;
    fixture.detectChanges();
    const label = fixture.nativeElement.querySelector('.progress-label');
    expect(label).toBeTruthy();
    expect(label.textContent).toContain('42.5');
  });

  it('should hide the label when showLabel is false', () => {
    component.showLabel = false;
    fixture.detectChanges();
    const label = fixture.nativeElement.querySelector('.progress-label');
    expect(label).toBeNull();
  });
});
