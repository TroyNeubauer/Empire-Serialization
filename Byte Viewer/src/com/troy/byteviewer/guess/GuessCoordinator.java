package com.troy.byteviewer.guess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.io.in.InputStreamInput;

public class GuessCoordinator {
	public static final List<Class<AbstractGuess>> guessClasses = new ArrayList<Class<AbstractGuess>>();

	static {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AssignableTypeFilter(AbstractGuess.class));

		Set<BeanDefinition> components = provider.findCandidateComponents("com/troy/byteviewer/guesses");
		boolean hasSubclass = false;
		for (BeanDefinition component : components) {
			try {
				Class<?> cls = Class.forName(component.getBeanClassName());
				if (AbstractGuess.class.isAssignableFrom(cls)) {
					Class<AbstractGuess> c = (Class<AbstractGuess>) cls;
					System.out.println(c);
					guessClasses.add(c);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private ArrayList<AnnotatedSection>[] sections;
	private static final long BYTES_PER_SECTION = 1000;
	private ArrayList<AbstractGuess> guesses = new ArrayList<AbstractGuess>();
	private Input[] inputs;

	public GuessCoordinator(long length, File file) {
		sections = new ArrayList[(int) (length / BYTES_PER_SECTION + 1L)];
		for (Class<AbstractGuess> clazz : guessClasses) {
			try {
				guesses.add(clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		inputs = new Input[guessClasses.size()];
		for (int i = 0; i < inputs.length; i++) {
			try {
				inputs[i] = new InputStreamInput(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void ensureAnalyzed(long maxOffset) {
		int i = 0;
		for (AbstractGuess guess : guesses) {
			guess.read(inputs[i++], maxOffset);
			List<AnnotatedSection> s = guess.getGuesses();
			for (AnnotatedSection section : s) {
				int start = (int) (section.getOffset() / BYTES_PER_SECTION);
				int end = (int) ((section.getOffset() + section.getLength()) / BYTES_PER_SECTION);
				for (int j = start; j < end; j++) {
					sections[j].add(section);
				}
			}
			guess.getGuesses().clear();
		}
	}

	public List<AnnotatedSection> getAnnotations(long offset, long length) {
		int start = (int) (offset / BYTES_PER_SECTION);
		int end = (int) ((offset + length) / BYTES_PER_SECTION);
		List<AnnotatedSection> result = new ArrayList<AnnotatedSection>();
		for (int i = start; i < end; i++) {
			for (AnnotatedSection section : sections[i]) {
				if (section.getOffset() + section.getLength() >= offset && section.getOffset() < offset + length) {
					result.add(section);
				}
			}
		}
		return result;
	}

}
